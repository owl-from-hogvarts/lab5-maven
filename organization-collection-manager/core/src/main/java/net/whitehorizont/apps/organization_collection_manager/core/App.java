package net.whitehorizont.apps.organization_collection_manager.core;

import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.Collection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationDataSinkSourceFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

public class App {
  public static void main(String[] args) throws StorageInaccessibleError {
    final var organisationDataSinkFactory = new OrganisationDataSinkSourceFactory();
    final var xmlCollectionAdapter = new CollectionAdapter<>(organisationDataSinkFactory);
    final IBaseStorage<Collection<Builder, OrganisationElement>, BaseId, CollectionMetadata> testStorage = new FileStorage<>("./test.xml", xmlCollectionAdapter);
    final CollectionManager<Collection<Builder, OrganisationElement>, IBaseStorage<Collection<Builder, OrganisationElement>, BaseId, CollectionMetadata>, BaseId> collectionManager = new CollectionManager<>();
    collectionManager.addStorage(testStorage);
    collectionManager.getCollection();
  }
}
