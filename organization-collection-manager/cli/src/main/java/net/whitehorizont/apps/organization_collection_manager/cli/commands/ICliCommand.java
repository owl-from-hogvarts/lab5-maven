package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public interface ICliCommand<@NonNull T, DM extends CliDependencyManager<?>> {
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
   * @throws IOException
   * @throws StorageInaccessibleError
   */
  @Nullable ICommand<T> getActualCommand(DM dependencyManager, Stack<String> arguments) throws IOException, StorageInaccessibleError;
}
