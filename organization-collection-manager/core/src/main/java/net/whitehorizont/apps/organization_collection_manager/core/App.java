package net.whitehorizont.apps.organization_collection_manager.core;

import net.whitehorizont.apps.organization_collection_manager.core.collection.Collection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationDataSinkSourceFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

public class App {
  public static void main(String[] args) throws StorageInaccessibleError {
    final var organisationDataSinkFactory = new OrganisationDataSinkSourceFactory();
    final var xmlCollectionAdapter = new CollectionAdapter<>(organisationDataSinkFactory);
    final var testStorage = new FileStorage<>("./test.xml", xmlCollectionAdapter);
    final CollectionManager<Collection<Builder, OrganisationElement>> collectionManager = new CollectionManager<>();
    collectionManager.addStorage(testStorage);
   
    final var defaultCollection$ = collectionManager.getCollection();
    defaultCollection$.subscribe((collection) -> {
      final var testElement = new OrganisationElement.Builder()
          .name("White Horizont holding")
          .ID(new UUID_ElementId());

      collection.getDataSink().supply(testElement);
      final var collectionId = collection.getMetadataSnapshot().getId();
      collectionManager.save(collectionId);
    });
  }
}
