package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

/**
 * Looks up storage for collection.
 * 
 * 
 * Provides abstraction over available stores
 * passes on operations on collections to appropriate storage.
 */
public class CollectionManager<C extends IBaseCollection<?, ?, ?>, S extends IBaseStorage<C, K, ?>, K extends BaseId> {
  private Set<S> storageSet;

  // makes collections available for loading
  public void addStorage(S storage, DataSinkSource<?, ?, C> dataSink) {
    // create appropriate mapping for storage
    storageSet.add(storage);
  }

  /**
   * Get collection by id
   * @throws CollectionNotFound
   */
  public C getCollection(K id) throws CollectionNotFound {
    // if no collection specified, return default collection
    return getCollectionAndStorage(id).blockingGet().getValue1();
  }

  private Single<Pair<S, C>> getCollectionAndStorage(K collectionId) throws CollectionNotFound {
    List<Observable<Pair<S, C>>> collections = new ArrayList<>();
    
    for (var store : storageSet) {
      final var collection$ = store.load(collectionId);
      collections.add(collection$.map((collection) -> new Pair<>(store, collection)));
    }

    // TODO: handle collection id collisions (when same collection somehow stored in multiple storages)
    return Observable.mergeDelayError(collections).first(null);
  }

  /**
   * Saves collection by collection id
   * @throws CollectionNotFound
   */
  public void save(K collectionId) throws CollectionNotFound {
    final var storageAndCollection = getCollectionAndStorage(collectionId).blockingGet();
    final var storage = storageAndCollection.getValue0();
    final var collection = storageAndCollection.getValue1();

    storage.save(collection);
    // Single.just(collection).doOnSuccess(storage).;
    // new SaveCommand
    // commandQueue.getErrors$().subscribe(, reportErrors)
    // commandQueue.add(saveCommand)
    // command.execute().catch(errors)
    // collectionManager.save(collection.getMetadataSnapshot().getId())
  }
}
