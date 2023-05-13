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
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public class Greeter {
  private static final String DEFAULT_PROMPT = " > ";
  private final LineReader reader;
  private final Map<String, ICliCommand<?>> commands;

  private static int convertBoolean(boolean b) {
    return b ? 1 : 0;
  }

  public Greeter(Map<String, ICliCommand<?>> commands, InputStream in, PrintStream out, PrintStream err)
      throws IOException {
    this.commands = commands;
    commands.put(HelpCommand.HELP_COMMAND, new HelpCommand(commands));
    commands.put(Exit.EXIT_COMMAND, new Exit());

    final Terminal defaultTerminal = TerminalBuilder.builder().system(true).streams(in, out).build();
    this.reader = new LineReaderImpl(defaultTerminal);
  }

  public Optional<ICommand<?>> promptCommand() throws IncorrectNumberOfArguments, UnknownCommand {
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

      return Optional.ofNullable(commandDescriptor.getActualCommand(wordsStack, getInputStream(), getOutputStream()));
    } catch(UserInterruptException|EndOfFileException e) {
      return onInterop();
    }
  }

  public OutputStream getErrorStream() {
    return reader.getTerminal().output();
  }

  private InputStream getInputStream() {
    return this.reader.getTerminal().input();
  }

  private OutputStream getOutputStream() {
    return this.reader.getTerminal().output();
  }

  private Optional<ICommand<?>> onInterop() {
    final var exitDescriptor = this.commands.get(Exit.EXIT_COMMAND);
    assert exitDescriptor != null;

    return Optional.ofNullable(exitDescriptor.getActualCommand(new Stack<>(), getInputStream(), getOutputStream()));

  }

  private static class HelpCommand implements ICliCommand<Void> {

    private static final String DESCRIPTION = "prints this help message";
    private static final int INDENT_SIZE = 2;
    private static final String INDENT_SYMBOL = " ";
    private static final String INDENT = INDENT_SYMBOL.repeat(INDENT_SIZE);
    private static final String DESCRIPTION_SEPARATOR = "-";
    private static final String WORD_SEPARATOR = " ";
    private static final String HELP_COMMAND = "help";

    private final Map<String, ICliCommand<?>> commands;

    HelpCommand(Map<String, ICliCommand<?>> commands) {
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
    public ICommand<Void> getActualCommand(Stack<String> arguments, InputStream in, OutputStream out) {
      final var output = new PrintStream(out);
      for (final var command : commands.entrySet()) {
        output.println(buildCommandDescription(command));
      }

      return null;
    }

    private String buildCommandDescription(Entry<String, ICliCommand<?>> command) {
      final List<String> words = new ArrayList<>();
      words.add(command.getKey());
      words.add(DESCRIPTION_SEPARATOR);
      words.add(command.getValue().getCommandDescription());

      final var commandDescription = String.join(WORD_SEPARATOR, words);
      return INDENT + commandDescription;

    }

  }
}
