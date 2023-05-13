package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.organization_collection_manager.core.commands.ExitCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public class Exit implements ICliCommand<Void> {
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
  public @Nullable ICommand<Void> getActualCommand(Stack<String> arguments, InputStream in, OutputStream out) {
    final var output = new PrintStream(out);
    output.println(EXIT_MESSAGE);
    return new ExitCommand();
  }
  
}
