package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Exit;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class Greeter<CM extends ICollectionManager<?, ?>> {
  private static final String DEFAULT_PROMPT = " > ";
  private final LineReader reader;
  private final Map<String, ICliCommand<?, CM>> commands;
  private final CliDependencyManager<CM> dependencyManager;

  private static int convertBoolean(boolean b) {
    return b ? 1 : 0;
  }

  public Greeter(CliDependencyManager<CM> dependencyManager, Map<String, ICliCommand<?, CM>> commands, InputStream in,
      PrintStream out, PrintStream err)
      throws IOException {
    this.dependencyManager = dependencyManager;

    this.commands = commands;
    commands.put(HelpCommand.HELP_COMMAND, new HelpCommand(commands));
    commands.put(Exit.EXIT_COMMAND, new Exit());

    final Terminal defaultTerminal = TerminalBuilder.builder().system(true).streams(in, out).build();
    this.reader = new LineReaderImpl(defaultTerminal);
  }

  public Optional<ICommand<?>> promptCommand()
      throws IncorrectNumberOfArguments, UnknownCommand, IOException, StorageInaccessibleError {
    try {
      final String userInput = reader.readLine(DEFAULT_PROMPT).trim().toLowerCase();

      final List<String> words = Arrays.asList(userInput.split(" "));
      // first pop command and then first argument and then second and so on
      Collections.reverse(words);
      final Stack<String> wordsStack = new Stack<String>();
      wordsStack.addAll(words);

      if (wordsStack.size() < 1 || userInput.length() < 1) {
        return Optional.empty();
      }

      final String command = wordsStack.pop();
      if (!commands.containsKey(command)) {
        throw new UnknownCommand(command);
      }

      final var commandDescriptor = commands.get(command);
      if (convertBoolean(commandDescriptor.hasArgument()) != wordsStack.size()) {
        // 1 because all command accept either one or zero arguments
        throw new IncorrectNumberOfArguments(command, commandDescriptor.hasArgument() ? 1 : 0, wordsStack.size());
      }

      return Optional.ofNullable(
          commandDescriptor.getActualCommand(this.dependencyManager, wordsStack, reader));
    } catch (UserInterruptException | EndOfFileException e) {
      return onInterop();
    }
  }

  public OutputStream getErrorStream() {
    return reader.getTerminal().output();
  }

  private Optional<ICommand<?>> onInterop() throws IOException, StorageInaccessibleError {
    final var exitDescriptor = this.commands.get(Exit.EXIT_COMMAND);
    assert exitDescriptor != null;

    return Optional.of(
        exitDescriptor.getActualCommand(dependencyManager, new Stack<>(), reader));

  }

  private static class HelpCommand<CM extends ICollectionManager<?, ?>> implements ICliCommand<Void, CM> {

    private static final String DESCRIPTION = "prints this help message";
    private static final int INDENT_SIZE = 2;
    private static final String INDENT_SYMBOL = " ";
    private static final String INDENT = INDENT_SYMBOL.repeat(INDENT_SIZE);
    private static final String DESCRIPTION_SEPARATOR = "-";
    private static final String WORD_SEPARATOR = " ";
    private static final String HELP_COMMAND = "help";

    private final Map<String, ICliCommand<?, ICollectionManager<?, ?>>> commands;

    HelpCommand(Map<String, ICliCommand<?, ICollectionManager<?, ?>>> commands) {
      this.commands = commands;
    }

    @Override
    public boolean hasArgument() {
      return false;
    }

    @Override
    public String getCommandDescription() {
      return DESCRIPTION;
    }

    @Override
    public ICommand<Void> getActualCommand(CliDependencyManager<CM> dependencyManager, Stack<String> arguments, LineReader lineReader) {
      
      final var commandDescriptions = new ArrayList<Pair<String, String>>();
      for (final var command : commands.entrySet()) {
        commandDescriptions.add(getCommandDescription(command));
      }

      final var output = lineReader.getTerminal().writer();
      final var maxCommandNameLength = commandDescriptions.stream().map(command -> command.getValue0().length()).max(Integer::compare).get().intValue();
      for (final var commandDescription : commandDescriptions) {
        final var commandNamePadded = padStart(commandDescription.getValue0(), maxCommandNameLength, WORD_SEPARATOR);
        final var descriptionArray = new ArrayList<String>();
        descriptionArray.add(commandNamePadded);
        descriptionArray.add(DESCRIPTION_SEPARATOR);
        descriptionArray.add(commandDescription.getValue1());
        
        final var description = String.join(WORD_SEPARATOR, descriptionArray);
        final var descriptionIndented = INDENT + description;

        output.println(descriptionIndented);
      }

      return null;
    }

    private Pair<String, String> getCommandDescription(
        Entry<String, ICliCommand<?, ICollectionManager<?, ?>>> command) {
      return new Pair<String, String>(command.getKey(), command.getValue().getCommandDescription());
    }

    private static final String padEnd(String string, int targetLength, String padString) {
      final var padding = computePaddingString(string.length(), targetLength, padString);
      return string + padding;
    }

    private static final String padStart(String string, int targetLength, String padString) {
      final var padding = computePaddingString(string.length(), targetLength, padString);
      return padding + string;
    }

    private static final String computePaddingString(int originalLength, int targetLength, String padString) {
      final var difference = targetLength - originalLength;

      if (difference <= 0) {
        return "";
      }

      return padString.repeat(difference);
    }
  }
}
