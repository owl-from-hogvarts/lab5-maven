package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.UpdateCommand;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class Update extends InputElementCommand implements ICliCommand<CliDependencyManager<?>> {
  public Update(Retries retries) {
    super(retries);
  }

  private static final String DESCRIPTION = "replace element with id by new one";
  

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager, Stack<String> arguments) throws Exception {
    final String idString = arguments.pop().trim();
    dependencyManager.getCollectionManager().getCollection().flatMap(collection -> {
      // parse id
      final var id = collection.getElementIdFromString(idString);

      final var prototype = collection.getElementPrototype();
      final var lineReader = dependencyManager.getGenericLineReader();

      try {
        final Streams streams = prepareStreams(dependencyManager);
  
        promptForFields(prototype, lineReader, streams);
      } catch (ValidationError e) {
        return Observable.error(e);
      }

      final var collectionReceiver = new CollectionCommandReceiver<>(collection);
      final var updateCommand = new UpdateCommand<>(id, prototype, collectionReceiver);
      
      // return dependencyManager.getCommandQueue().push(updateCommand);
    });
    // get key-element pair by id
    // hold over deletion of element from collection
    // ask for new element via insert command
    // update new's element id with the old one
    // propose new element to collection
  }
  
}
