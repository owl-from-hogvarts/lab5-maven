package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class Insert
    extends InputElementCommand implements ICliCommand {
  
  public Insert(Retries retries) {
    super(retries);
  }

  private static final String DESCRIPTION = "insert element into collection";

  @Override
  public <DM extends CliDependencyManager<?>> Observable<Void> run(DM dependencyManager, Stack<String> arguments)
      throws StorageInaccessibleError, ValidationError {

    final var collectionManager = getCollectionManager(dependencyManager);
    final var collection = getCollection(collectionManager);
    final var key = collection.getElementKeyFromString(arguments.pop());

    try {
      final var insertCommand = getInsertCommand(key, collection, dependencyManager);

      return dependencyManager.getCommandQueue().push(insertCommand);
    } catch (ValidationError e) {

      return Observable.error(e);
    }
  }

  private <P extends IElementPrototype<?>> InsertCommand<P> getInsertCommand(BaseId key, ICollection<P, ?, ?> collection, CliDependencyManager<?> dependencyManager) throws ValidationError {
    final var prototype = collection.getElementPrototype();
    final var lineReader = dependencyManager.getGenericLineReader();
    final Streams streams = prepareStreams(dependencyManager);
  
    promptForFields(prototype, lineReader, streams);
    
    final var receiver = new CollectionCommandReceiver<>(collection);
    return new InsertCommand<P>(key, prototype, receiver);
  }

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  // receive streams
  // get collection we operating on
  // on that collection get element prototype
  // for each field of element prototype:
  // if field contains children
  // ...
  // if not, retrieve field's metadata.
  // with that metadata (field name especially) ask for input
  // how prompt knows witch type to return?
  // We create driver for that prompt, witch maps known types to prompt calls
}
