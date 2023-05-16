package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Exit;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Help;
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
  private final Map<String, ICliCommand<?, ? super CliDependencyManager<CM>>> commands;
  private final CliDependencyManager<CM> dependencyManager;

  private static int convertBoolean(boolean b) {
    return b ? 1 : 0;
  }

  public Greeter(CliDependencyManager<CM> dependencyManager)
      throws IOException {
    this.dependencyManager = dependencyManager;
    this.reader = dependencyManager.getLineReader();

    this.commands = dependencyManager.getCommands();
    final ICliCommand<Void, ? super CliDependencyManager<CM>> help = new Help();
    commands.put(Help.HELP_COMMAND, help);
    commands.put(Exit.EXIT_COMMAND, new Exit());
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
          commandDescriptor.getActualCommand(this.dependencyManager, wordsStack));
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
        exitDescriptor.getActualCommand(dependencyManager, new Stack<>()));

  }

  
}
