package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class Insert<P extends IElementPrototype<?>, CM extends ICollectionManager<? extends ICollection<P, ?, ?>, ?>>
    extends InputElementCommand implements ICliCommand<CliDependencyManager<CM>> {
  
      public Insert(Retries retries) {
    super(retries);
  }

  private static final String DESCRIPTION = "insert element into collection";

  @Override
  public Observable<Void> run(CliDependencyManager<CM> dependencyManager, Stack<String> arguments)
      throws StorageInaccessibleError {

    final var collectionManager = dependencyManager.getCollectionManager();
    final ICollection<P, ?, ?> collection = collectionManager.getCollection().blockingFirst();

    final var prototype = collection.getElementPrototype();
    final var lineReader = dependencyManager.getGenericLineReader();

  try {
      final Streams streams = prepareStreams(dependencyManager);

      promptForFields(prototype, lineReader, streams);
    } catch (ValidationError e) {
      return Observable.error(e);
    }

    final var collectionReceiver = new CollectionCommandReceiver<>(collection);
    final var insertCommand = new InsertCommand<>(prototype, collectionReceiver);

    return dependencyManager.getCommandQueue().push(insertCommand);
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
