package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class CollectionManagerReceiver<C extends ICollection<?, ?, ?>> implements ICollectionManager<C> {

  private final ICollectionManager<C> collectionManager;

  public CollectionManagerReceiver(ICollectionManager<C> collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public Observable<C> getCollection() throws StorageInaccessibleError {
    return collectionManager.getCollection();
  }

  @Override
  public void save(BaseId collectionId) throws CollectionNotFound, StorageInaccessibleError {
    collectionManager.save(collectionId);
  }
  
}
