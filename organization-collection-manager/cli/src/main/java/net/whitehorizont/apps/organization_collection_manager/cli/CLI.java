package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Exit;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Help;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IGlobalErrorHandler;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IInterruptHandler;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;

@NonNullByDefault
public class CLI<CM extends ICollectionManager<?, ?>> {
  private static final String DEFAULT_PROMPT = " > ";
  private final LineReader reader;
  private final Map<String, ICliCommand<? super CliDependencyManager<CM>>> commands;
  private final CliDependencyManager<CM> dependencyManager;
  private final PrintStream err;
  private final IInterruptHandler interruptHandler;
  private final IGlobalErrorHandler globalErrorHandler;

  private static int convertBoolean(boolean b) {
    return b ? 1 : 0;
  }

  public void start() {
    while (true) {
      try {
        promptCommand().blockingSubscribe();
      } catch (Throwable e) {
        if (globalErrorHandler.handle(e, dependencyManager)) {
          break;
        }
      }
    }
  }
  

  public CLI(CliDependencyManager<CM> dependencyManager)
      throws IOException {
    this.dependencyManager = dependencyManager;
    this.reader = dependencyManager.getLineReader();
    this.err = new PrintStream(this.dependencyManager.getStreams().err);

    this.interruptHandler = dependencyManager.getOnInterrupt().isPresent() 
                              ? dependencyManager.getOnInterrupt().get()
                              : this::onInterop;

    this.globalErrorHandler = dependencyManager.getGlobalErrorHandler();

    this.commands = dependencyManager.getCommands();
    final ICliCommand<? super CliDependencyManager<CM>> help = new Help();
    commands.put(Help.HELP_COMMAND, help);
    commands.put(Exit.EXIT_COMMAND, new Exit());
  }

  public Observable<Void> promptCommand()
      throws Exception {
    try {
      final String userInput = reader.readLine(DEFAULT_PROMPT).trim().toLowerCase();

      // separate string into command and the reminder
      // everything after the command is single argument
      // !!! THIS IS NOT MAGIC NUMBER !!!
      // IT IS EXPECTED TO BE HARDCODED
      final List<String> words = Arrays.asList(userInput.split(COMMAND_SEPARATOR, 2));
      // first pop command and then first argument and then second and so on
      Collections.reverse(words);
      final Stack<String> wordsStack = new Stack<String>();
      wordsStack.addAll(words);

      if (wordsStack.size() < 1 || userInput.length() < 1) {
        return Observable.empty();
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

      return commandDescriptor.run(this.dependencyManager, wordsStack);
    } catch (UserInterruptException | EndOfFileException e) {
      return this.interruptHandler.handle();
    }
  }

  public static boolean defaultGlobalErrorHandler(Throwable e, CliDependencyManager<?> dependencyManager) {
    final var err = dependencyManager.getStreams().err;
    err.println("Error: " + e.getMessage());
    return false;
  }

  public OutputStream getErrorStream() {
    return reader.getTerminal().output();
  }

  private Observable<Void> onInterop() throws Exception {
    final var exitDescriptor = this.commands.get(Exit.EXIT_COMMAND);
    assert exitDescriptor != null;

    return exitDescriptor.run(dependencyManager, new Stack<>());

  }

}
