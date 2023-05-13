package net.whitehorizont.apps.organization_collection_manager.core;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.collection.collection_manager.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CommandQueue;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class App {
  public static void main(String[] args) throws StorageInaccessibleError {
    final OrganisationElementFactory organisationElementFactory = new OrganisationElementFactory();
    final var xmlCollectionAdapter = new CollectionAdapter<>(organisationElementFactory);
    final var testStorage = new FileStorage<>("./test.xml", xmlCollectionAdapter);
    final ICollectionManager<RamCollection<Builder, OrganisationElement>, CollectionMetadata> collectionManager = new CollectionManager<>();
    testStorage.loadMetadata();
    collectionManager.addStorage(testStorage);

    final var commandQueue = new CommandQueue();

    final var defaultCollection$ = collectionManager.getCollection();
    defaultCollection$.subscribe((collection) -> {
      // builder = collection.getDataSink().getBuilder()
      // for (final var field : builder.getFields())
      // TODO: handle exception
      final var testElement = new OrganisationElement.Builder()
          .name("")
          .ID(new UUID_ElementId());
          
      final var collectionReceiver = new CollectionCommandReceiver<>(collection);
      // final var testCommand = new
      // LoadCommand<OrganisationElement>(collectionReceiver);
      final var testCommand = new InsertCommand<>(testElement, collectionReceiver);
      commandQueue.push(testCommand).subscribe(System.out::println);

      // collectionManager.save(collection.getMetadataSnapshot().getId());
    });
    // sometimes java can't infer types for us so we should help it to kill itself
    // and write in some better language

    // get collection
    // get prototype for element
    // get prototype's fields (getFields)
    // for each field
    // get metadata
    // supply prompt with provided metadata and field's value primitive type
    // prompt will ask user for input and transform value to desired type
    // receive value from prompt
    // call setValue for field with supplied value
    // when element is build
    // supply it to collection's dataSink
    // dataSink will construct actual element and request element validation

  }
}
