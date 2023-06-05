package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ShowCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class Show extends BaseElementCommand 
    implements ICliCommand<CollectionCommandReceiver<?, ?>> {
  private static final String DESCRIPTION = "print all collection elements";

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(
      CliDependencyManager<? extends CollectionCommandReceiver<?, ?>> dependencyManager,
      Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
    return Observable.create(subscriber -> {
      Observable.just(dependencyManager.getCollectionReceiver()).subscribe(receiver -> {
        final var show = new ShowCommand<>(receiver);
        final var out = dependencyManager.getStreams().out;
        dependencyManager.getCommandQueue().push(show).subscribe(keyElement -> {
          final var element = keyElement.getValue();
          printFields(element.getTree(), keyElement.getKey(), out);
        });

        subscriber.onComplete();
      });
    });
  }
}
