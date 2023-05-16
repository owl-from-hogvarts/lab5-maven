package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ExitCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public class Exit implements ICliCommand<Void, CliDependencyManager<?>> {
  public static final String EXIT_COMMAND = "exit";
  private static final String DESCRIPTION = "exit without saving";
  private static final String EXIT_MESSAGE = "Exiting without saving!";

  @Override
  public boolean hasArgument() {
    return false;
  }
  

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public @Nullable ICommand<Void> getActualCommand(CliDependencyManager<?> dependencyManager, Stack<String> arguments) {
    final var lineReader = dependencyManager.getLineReader();
    final var output = lineReader.getTerminal().writer();
    output.println(EXIT_MESSAGE);
    output.flush();
    return new ExitCommand();
  }
  
}
