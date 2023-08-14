package net.whitehorizont.apps.collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

/**
 * Looks up storage for collection.
 * 
 * 
 * Provides abstraction over available stores
 * passes on operations on collections to appropriate storage.
 */
@NonNullByDefault
public class CollectionManager<C extends ICollection<?>>
    implements ICollectionManager<C> {
  private final IBaseStorage<C> storage;
  private final C collection;

  // makes collections in source available for loading

  public CollectionManager(IBaseStorage<C> storage) {
    this.storage = storage;
    collection = storage.load().blockingFirst();
  }

  public Observable<C> getCollection() throws StorageInaccessibleError {
    return Observable.just(collection);
  }


  /**
   * Saves collection by collection id
   * 
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError
   */

  public void save(BaseId collectionId) throws CollectionNotFound, StorageInaccessibleError {

    storage.save(collection);
    // new SaveCommand
    // result$ = commandQueue.add(saveCommand)
    // commandQueue.getErrors$().subscribe(, reportErrors)
    // commandQueue.add(saveCommand)
    // command.execute().catch(errors)
    // collectionManager.save(collection.getMetadataSnapshot().getId())
  }
}

