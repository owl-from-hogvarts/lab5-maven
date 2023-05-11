package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public interface ICliCommand<@NonNull T, P> {
  Iterable<CommandArgumentDescriptor> getArgumentDescriptors();
  /**
   * provide full access to input and output for command to 
   * freely communicate with user 
   * 
   * @param in
   * @param out
   * @param err
   * @return
   */
  ICommand<T> getActualCommand(Map<String, CommandArgument> arguments, InputStream in, PrintStream out, PrintStream err);
}
