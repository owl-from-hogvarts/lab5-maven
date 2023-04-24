package net.whitehorizont.apps.organization_collection_manager.core.storage;


import org.eclipse.jdt.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;


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
 * @param <K> key by which collection can be addressed
 * @param <M> collection metadata (pray on type inference)
 */
public interface IBaseStorage<C extends IBaseCollection<?, ?, M>, K extends BaseId, M extends IWithId<K>> {
  /**
   * Loads default collection. 
   * 
   * Implementation choose default collection 
   */
  Observable<C> load() throws CollectionNotFound;
  Observable<C> load(K key) throws CollectionNotFound;

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
   * Useful for ui for user to choose collection
   * @return
   */
  Observable<M> loadMetadata() throws CollectionNotFound;
  Observable<M> loadMetadata(K key) throws CollectionNotFound;

  void save(@NonNull C collection);
}
