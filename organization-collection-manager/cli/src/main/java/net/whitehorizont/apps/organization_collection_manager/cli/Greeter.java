package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public class Greeter {
  private static final String DEFAULT_PROMPT = " > ";
  private final LineReader reader;
  private final Map<String, ICliCommand<?>> commands;

  public Greeter(Map<String, ICliCommand<?>> commands, InputStream in, PrintStream out, PrintStream err) throws IOException {
    this.commands = commands;
    final Terminal defaultTerminal = TerminalBuilder.builder().system(false).streams(in, out).build(); 
    this.reader = new LineReaderImpl(defaultTerminal);
  }
  
  public Optional<ICommand<?>> promptCommand() throws IncorrectNumberOfArguments {
    final String userInput = reader.readLine(DEFAULT_PROMPT).trim().toLowerCase(null);

    if (userInput.length() < 1) {
      return Optional.empty();
    }
    
    final List<String> words = Arrays.asList(userInput.split(" "));
    // to first pop command and then first argument and then second and so on 
    Collections.reverse(words);
    final Stack<String> wordsStack = new Stack<String>();
    wordsStack.addAll(words);
    

    final String command = wordsStack.pop();
    final var commandDescriptor = commands.get(command);
    final var argumentsExpected = commandDescriptor.getRequiredArgumentsCount();
    final var argumentsMaximum  = commandDescriptor.getMaxArgumentCount();

    if (argumentsExpected > wordsStack.size() || argumentsMaximum < wordsStack.size()) {
      throw new IncorrectNumberOfArguments(command, argumentsExpected, wordsStack.size(), argumentsMaximum);
    }

    final var terminal = reader.getTerminal();
    return Optional.of(commandDescriptor.getActualCommand(wordsStack, terminal.input(), terminal.output()));
  }
}
