package net.whitehorizont.apps.organization_collection_manager.core.storage;


import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;


/**
 * Provides abstraction over source of single or 
 * multiple collections
 * order of collections is not guaranteed
 * storage can report interactive warnings and errors
 * provides collections via observable
 * 
 * Storage is responsible for creating/initializing collections
 * 
 * @param <C> collection type
 */
@NonNullByDefault
public interface IBaseStorage<C extends ICollection<?, ?>> {
  /**
   * Loads default collection. 
   * 
   * Implementation choose default collection 
   */
  Observable<C> load();
  Observable<C> load(BaseId key) throws CollectionNotFound;
  /**
   * Loads collection with id. If it does not exist, creates 
   * new collection with specified id and returns it
   * 
   */
  Observable<C> loadSafe(CollectionMetadata metadata);

  /**
   * Loads all collections. USE WITH CAUTION!
   *
   * Retrieving all collections may hit performance 
   * @return
   */
  Observable<C> loadAll();
  /**
   * Loads only collection metadata without body. 
   * 
   * When key not supplied, gets default collection metadata
   * Useful for ui for user to choose collection
   * @return
   */
  Observable<CollectionMetadata> loadMetadata() throws CollectionNotFound;
  Observable<CollectionMetadata> loadMetadata(BaseId key) throws CollectionNotFound;

  void save(C collection) throws StorageInaccessibleError;
}
