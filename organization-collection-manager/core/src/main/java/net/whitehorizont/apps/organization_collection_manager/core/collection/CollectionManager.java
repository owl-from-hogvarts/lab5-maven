package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;

/**
 * Looks up storages for collection.
 * 
 * 
 * Provides abstraction over available stores
 * passes on operations on collections to appropriate storage.
 */
public class CollectionManager<T, E extends IWithId<? extends ISerializableKey>> {
  private Map<IBaseStorage<Collection<T, E>>, Set<Collection<T, E>>> storageToCollectionMap;

  // makes collections available for loading
  public void addStorage(IBaseStorage<Collection<T, E>> storage, DataSinkSource<T, E, Collection<T, E>> dataSink) {
    // create appropriate mapping for storage
    final var collectionSet = new HashSet<Collection<T, E>>();
    storageToCollectionMap.put(storage, collectionSet);

    // request default collection

    // when storage is ready, request all collection prototypes it stores
    // if nothing provided
      // create at least one empty collection
    // create collection from prototype. May be wrap elements into insert command
    // supply insert command with appropriate collection
    // publish commands on queue
  }

  /**
   * Get collection by id
   * @throws NoSuchCollection
   */
  public IBaseCollection<T, E> getCollection(CollectionId id) throws NoSuchCollection {
    // if no collection specified, return default collection
    return getCollectionAndStorage(id).getValue1();
    
  }

  private Pair<IBaseStorage<Collection<T, E>>, Collection<T, E>> getCollectionAndStorage(CollectionId collectionId) throws NoSuchCollection {
    for (var entry : storageToCollectionMap.entrySet()) {
      for (var collection : entry.getValue()) {
        if (collection.getMetadataSnapshot().getId().equals(collectionId)) {
          return new Pair<IBaseStorage<Collection<T, E>>,Collection<T,E>>(entry.getKey(), collection);
        }
      }
    }

    throw new NoSuchCollection();
  }

  /**
   * Saves collection by collection id
   * @throws NoSuchCollection
   */
  public void save(CollectionId collectionId) throws NoSuchCollection {
    final var storageAndCollection = getCollectionAndStorage(collectionId);
    final var storage = storageAndCollection.getValue0();
    final var collection = storageAndCollection.getValue1();
    final var ready = Observable.empty();
    Observable.just(collection);
    // Single.just(collection).doOnSuccess(storage).;
    // new SaveCommand
    // commandQueue.getErrors$().subscribe(, reportErrors)
    // commandQueue.add(saveCommand)
    // command.execute().catch(errors)
    // collectionManager.save(collection.getMetadataSnapshot().getId())
  }
}
