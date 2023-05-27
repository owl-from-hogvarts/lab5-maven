package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionManagerReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.SaveCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class Save implements ICliCommand<CliDependencyManager<?>> {

  private static final String DESCRIPTION = "saves collection to disk";

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager, Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
    final var collectionManager = dependencyManager.getCollectionManager();
    final var collectionManagerReceiver = new CollectionManagerReceiver<>(collectionManager);
    final var save = new SaveCommand(collectionManagerReceiver);

    return dependencyManager.getCommandQueue().push(save);

  }
}
