package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.commands.ClearCommand;
import net.whitehorizont.apps.collection_manager.core.commands.CollectionCommandReceiver;

@NonNullByDefault
public class Clear implements ICliCommand<CollectionCommandReceiver<?>> {
  private static final String DESCRIPTION = "delete all elements from collection";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager, Stack<String> arguments) throws Exception {
    final var collectionReceiver = dependencyManager.getCollectionReceiver();
    final var clear = new ClearCommand(collectionReceiver);

    return dependencyManager.getCommandQueue().push(clear);
  }
  
}
