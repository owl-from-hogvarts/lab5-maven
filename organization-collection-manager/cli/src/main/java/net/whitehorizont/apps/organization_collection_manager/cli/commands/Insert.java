package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class Insert<Host extends ICollectionElement<Host>, WritableHost extends Host>
    extends InputElementCommand<Host, WritableHost> implements ICliCommand<CollectionCommandReceiver<Host>> {
  
  public Insert(MetadataComposite<?, Host, WritableHost> metadata, IWritableHostFactory<WritableHost> elementFactory, Retries retries) {
    super(metadata, elementFactory, retries);
  }

  private static final String DESCRIPTION = "insert element into collection";

  @Override
  public Observable<Void> run(CliDependencyManager<? extends CollectionCommandReceiver<Host>> dependencyManager, Stack<String> arguments)
      throws StorageInaccessibleError, ValidationError {

    final var collection = dependencyManager.getCollectionReceiver();
    final var key = collection.getElementKeyFromString(arguments.pop());

    try {
      final var insertCommand = getInsertCommand(key, collection, dependencyManager);

      return dependencyManager.getCommandQueue().push(insertCommand);
    } catch (ValidationError e) {

      return Observable.error(e);
    }
  }

  private InsertCommand<Host> getInsertCommand(ElementKey key, ICollection<Host> collection, CliDependencyManager<?> dependencyManager) throws ValidationError {
    final var lineReader = dependencyManager.getGenericLineReader();
    final Streams streams = prepareStreams(dependencyManager);
    
    final var host = getWritableCollectionElement();
    promptForFields(host, lineReader, streams);
    
    final var receiver = new CollectionCommandReceiver<>(collection);
    return new InsertCommand<>(key, host, receiver);
  }

  @Override
  public Optional<String> getArgument() {
    return Optional.of("key");
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
