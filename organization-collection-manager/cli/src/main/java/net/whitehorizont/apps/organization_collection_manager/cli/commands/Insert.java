package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;
import org.jline.reader.LineReader.Option;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

@NonNullByDefault
public class Insert<P extends IElementPrototype<?>, CM extends ICollectionManager<? extends ICollection<P, ?, ?>, ?>> implements ICliCommand<Void, CM> {
  private static final String DESCRIPTION = "insert element into collection";

  @Override
  public InsertCommand<P> getActualCommand(CliDependencyManager<CM> dependencyManager, Stack<String> arguments) throws IOException, StorageInaccessibleError {
    final var collectionManager = dependencyManager.getCollectionManager();
    
    
    final ICollection<P, ?, ?> collection = collectionManager.getCollection().blockingFirst();
    final var prototype = collection.getElementPrototype();
    final var fields = prototype.getWriteableFromStringFields();

    final var lineReader = dependencyManager.getLineReader();
    for (final var field : fields) {
      final var metadata = field.getMetadata();
      final var userInput = lineReader.readLine(metadata.getDisplayedName() + ": ").trim();

      try {
        field.setValue(userInput);
      } catch (ValidationError e) {
        final var output = lineReader.getTerminal().writer();
        output.println(e.getMessage());
      }
    }

    final var collectionReceiver = new CollectionCommandReceiver<>(collection);
    final var insertCommand = new InsertCommand<>(prototype, collectionReceiver);

    return insertCommand;
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
