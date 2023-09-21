package net.whitehorizont.apps.collection_manager.core.storage;


import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;


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
public interface IStorage<C extends ICollection<?>> {
  /**
   * Loads default collection. 
   * 
   * Implementation choose default collection 
   */
  Observable<C> load();
  /**
   * Loads only collection metadata without body. 
   * 
   * When key not supplied, gets default collection metadata
   * Useful for ui for user to choose collection
   * @return
   */
  Observable<CollectionMetadata> loadMetadata() throws CollectionNotFound;

  void save(C collection) throws StorageInaccessibleError;
}
