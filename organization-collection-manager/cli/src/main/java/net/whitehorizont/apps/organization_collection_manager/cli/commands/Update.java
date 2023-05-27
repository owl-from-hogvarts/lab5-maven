package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.UpdateCommand;

@NonNullByDefault
public class Update<P extends IElementPrototype<?>, CM extends ICollectionManager<? extends ICollection<P, ?, ?>, ?>>
 extends InputElementCommand implements ICliCommand<CliDependencyManager<CM>> {
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
  public Observable<Void> run(CliDependencyManager<CM> dependencyManager, Stack<String> arguments) throws Exception {
    final ICollection<P, ?, ?> collection = dependencyManager.getCollectionManager().getCollection().blockingFirst();
    
    final String idString = arguments.pop().trim();
    final var id = collection.getElementIdFromString(idString);

    final var collectionReceiver = new CollectionCommandReceiver<>(collection);
    final var lineReader = dependencyManager.getGenericLineReader();

    final var updateCommand = new UpdateCommand<>(id, collectionReceiver, (oldPrototype) -> {
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
  
}
