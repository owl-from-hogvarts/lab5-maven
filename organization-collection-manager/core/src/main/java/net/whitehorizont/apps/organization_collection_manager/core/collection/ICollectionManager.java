package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

// ! CollectionManager does NOT create collections by itself. 
// !It should choose storage and pass collection creation request to it

@NonNullByDefault
public interface ICollectionManager<C extends ICollection<?>> {

  /**
   * Loads default collection. 
   * If it does not exist, creates it
   * 
   * Manager choose storage on its own
   * 
   * @return
   * @throws StorageInaccessibleError
   */
  Observable<C> getCollection() throws StorageInaccessibleError;

  // no safe counterpart 'cause save should accept only known 
  // collections that is associated with certain store
  /**
   * Saves collection by collection id
   * 
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError
   */
  void save(BaseId collectionId) throws CollectionNotFound, StorageInaccessibleError;

}