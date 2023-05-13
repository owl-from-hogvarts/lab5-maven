package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;

@NonNullByDefault
public interface ICliCommand<@NonNull T> {
  boolean hasArgument();
  String getCommandDescription();

  /**
   * provide full access to input and output for command to 
   * freely communicate with user 
   * 
   * @param in
   * @param out
   * @param err
   * @return
   */
  @Nullable ICommand<T> getActualCommand(Stack<String> arguments, InputStream in, OutputStream out);
}
