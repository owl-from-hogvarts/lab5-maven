package net.whitehorizont.apps.collection_manager.cli.commands;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.commands.SaveCommand;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class Save implements ICliCommand<CollectionCommandReceiver<?>> {

  private static final String DESCRIPTION = "saves collection to disk";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager, Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
    final var collectionManagerReceiver = new CollectionManagerReceiver<>(dependencyManager.getCollectionManager());
    final var save = new SaveCommand(collectionManagerReceiver);

    return dependencyManager.getCommandQueue().push(save);

  }
}
