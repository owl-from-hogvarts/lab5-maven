package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;
import io.reactivex.rxjava3.annotations.Nullable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class OrganisationInsertAdapter implements InsertAdapter<Builder> {

  @Override
  public InsertCommand<Builder> getCommand(ICollectionManager<ICollection<Builder, ?, ?>, ?> collectionManager, Stack<String> arguments, LineReader reader)
      throws IOException, StorageInaccessibleError {

    final var organisationBuilder = new Builder();
    final var nameMetadata = OrganisationElement.getNameMetadata();

    @Nullable var nameInput = "";
    do {
      nameInput = reader.readLine(nameMetadata.getDisplayedName() + ": ").trim();
      if (nameInput.length() < 1) {
        if (nameMetadata.isNullable()) {
          nameInput = null;
          break;
        }

        reader.getTerminal().writer().println(nameMetadata.getOnNullMessage());
        
      }
    } while (nameInput.length() < 1);

    organisationBuilder.name(nameInput);

    final ICollection<Builder, ?, ?> collection = collectionManager.getCollection().blockingFirst();
    final var collectionReceiver = new CollectionCommandReceiver(collection);

    return new InsertCommand<OrganisationElement.Builder>(organisationBuilder, collectionReceiver);
  }

}
