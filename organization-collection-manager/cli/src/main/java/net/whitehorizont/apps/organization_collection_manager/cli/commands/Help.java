package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public class Help implements ICliCommand<Void, CliDependencyManager<?>> {

  private static final String DESCRIPTION = "prints this help message";
  private static final int INDENT_SIZE = 2;
  private static final String INDENT_SYMBOL = " ";
  private static final String INDENT = INDENT_SYMBOL.repeat(INDENT_SIZE);
  private static final String DESCRIPTION_SEPARATOR = "-";
  private static final String WORD_SEPARATOR = " ";
  public static final String HELP_COMMAND = "help";

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public ICommand<Void> getActualCommand(CliDependencyManager<?> dependencyManager, Stack<String> arguments) {
    
    final var commandDescriptions = new ArrayList<Pair<String, String>>();
    final Map<String, ? extends ICliCommand<?, ? extends CliDependencyManager<?>>> commands = dependencyManager.getCommands();
    for (final var command : commands.entrySet()) {
      final var commandDescription = getCommandDescription(command);
      commandDescriptions.add(commandDescription);
    }

    final var lineReader = dependencyManager.getLineReader();
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
      Entry<String, ? extends ICliCommand<?, ? extends CliDependencyManager<?>>> command) {
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