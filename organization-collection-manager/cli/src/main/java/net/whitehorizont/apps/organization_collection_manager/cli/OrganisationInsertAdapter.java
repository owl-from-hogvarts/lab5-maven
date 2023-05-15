package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;
import io.reactivex.rxjava3.annotations.Nullable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementRawData;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

@NonNullByDefault
public class OrganisationInsertAdapter implements InsertAdapter<OrganisationElementRawData> {

  @Override
  public InsertCommand<OrganisationElementRawData> getCommand(ICollectionManager<ICollection<OrganisationElementRawData, ?, ?>, ?> collectionManager, Stack<String> arguments, LineReader reader)
      throws IOException, StorageInaccessibleError {

      final var prototype = new OrganisationElement.OrganisationElementPrototype();
      final var fields = prototype.getWriteableFromStringFields();

      for (final var field : fields) {
        final var metadata = field.getMetadata();
        final var userInput = reader.readLine(metadata.getDisplayedName() + ": ").trim();

        try {
          field.setValue(userInput);
        } catch (ValidationError e) {
          final var output = reader.getTerminal().writer();
          output.println(e.getMessage());
        }
      }

      final var rawData = prototype.getRawElementData();
      final var collection = collectionManager.getCollection().blockingFirst();
      final var collectionReceiver = new CollectionCommandReceiver<>(collection);
      final var insertCommand = new InsertCommand<>(rawData, collectionReceiver);

      return insertCommand;
    }
}
