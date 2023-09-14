package net.whitehorizont.apps.collection_manager.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.commands.Exit;
import net.whitehorizont.apps.collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.collection_manager.cli.errors.IGlobalErrorHandler;
import net.whitehorizont.apps.collection_manager.cli.errors.IInterruptHandler;
import net.whitehorizont.apps.collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class CLI<DP> {
  private static final String DEFAULT_PROMPT = " > ";
  private static final String COMMAND_SEPARATOR = " ";
  private final LineReader reader;
  private final Map<String, ICliCommand<? super DP>> commands;
  private final CliDependencyManager<DP> dependencyManager;
  private final IInterruptHandler interruptHandler;
  private final IGlobalErrorHandler globalErrorHandler;

  private static int convertBoolean(boolean b) {
    return b ? 1 : 0;
  }

  public void start() {    
    while (true) {
      try {
        try {
          promptCommand().blockingSubscribe();
        } catch (UserInterruptException | EndOfFileException e) {
          this.interruptHandler.handle().blockingSubscribe();
          break;
        }
      } catch (Throwable e) {
        // errors from blocking subscribe are wrapped into runtime exception
        if (e instanceof RuntimeException && e.getCause() != null) {
          e = e.getCause();
        }
        if (globalErrorHandler.handle(e, dependencyManager)) {
          break;
        }
      }
    }
  }

  public CLI(CliDependencyManager<DP> dependencyManager)
      throws IOException {
    this.dependencyManager = dependencyManager;
    this.reader = dependencyManager.getCommandLineReader();

    this.interruptHandler = dependencyManager.getOnInterrupt().isPresent()
        ? dependencyManager.getOnInterrupt().get()
        : this::onInterop;

    this.globalErrorHandler = dependencyManager.getGlobalErrorHandler();

    this.commands = dependencyManager.getCommands();
  }

  public Observable<Void> promptCommand()
      throws Exception {
      final String userInput = reader.readLine(DEFAULT_PROMPT).trim().strip();

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
      if (convertBoolean(commandDescriptor.getArgumentName().isPresent()) != wordsStack.size()) {
        // 1 because all command accept either one or zero arguments
        throw new IncorrectNumberOfArguments(command, convertBoolean(commandDescriptor.getArgumentName().isPresent()), wordsStack.size());
      }

      return commandDescriptor.run(this.dependencyManager, wordsStack);
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
