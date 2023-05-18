package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public interface ICliCommand<DM extends CliDependencyManager<?>> {
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
  Observable<Void> run(DM dependencyManager, Stack<String> arguments) throws IOException, StorageInaccessibleError;
}
