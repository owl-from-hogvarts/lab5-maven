package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public interface ICliCommand<@NonNull T, CM extends ICollectionManager<?, ?>> {
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
  @Nullable ICommand<T> getActualCommand(CliDependencyManager<CM> dependencyManager, Stack<String> arguments, LineReader lineReader) throws IOException, StorageInaccessibleError;
}
