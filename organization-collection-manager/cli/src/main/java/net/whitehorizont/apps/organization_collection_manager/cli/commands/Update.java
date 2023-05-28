package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.UpdateCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver.IPrototypeCallback;

@NonNullByDefault
public class Update
 extends InputElementCommand implements ICliCommand {
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
  public <DM extends CliDependencyManager<?>> Observable<Void> run(DM dependencyManager, Stack<String> arguments) throws Exception {
    final var collectionManager = getCollectionManager(dependencyManager);
    final var collection = getCollection(collectionManager);
    
    final String idString = arguments.pop().trim();
    final var id = collection.getElementIdFromString(idString);

    final var lineReader = dependencyManager.getGenericLineReader();

    final var updateCommand = getUpdateCommand(collection, dependencyManager, id, (oldPrototype) -> {
      final var streams = prepareStreams(dependencyManager);
      promptForFields(oldPrototype, lineReader, streams);
      return oldPrototype;
    });

    return dependencyManager.getCommandQueue().push(updateCommand);

    // get key-element pair by id
    // hold over deletion of element from collection
    // ask for new element via insert command
    // update new's element id with the old one
    // propose new element to collection
  } 

  private <P extends IElementPrototype<?>> UpdateCommand<P> getUpdateCommand(ICollection<P, ?, ?> collection, CliDependencyManager<?> dependencyManager, BaseId id, IPrototypeCallback<P> callback) {
    final var receiver = new CollectionCommandReceiver<>(collection);
    return new UpdateCommand<P>(id, receiver, callback);
  }
}
