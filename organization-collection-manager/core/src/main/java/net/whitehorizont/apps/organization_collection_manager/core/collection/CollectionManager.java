package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IStorage;

/**
 * Maps storages to collections they hold.
 * 
 */
public class CollectionManager<T, E extends IWithId<? extends ISerializableKey>> {
  private Map<IStorage<Collection<T, E>>, Set<Collection<T, E>>> storageToCollectionMap;

  public void addStorage(IStorage<Collection<T, E>> storage, DataSinkSource<T, E, Collection<T, E>> dataSink) {
    // create appropriate mapping for storage
    final var collectionSet = new HashSet<Collection<T, E>>();
    storageToCollectionMap.put(storage, collectionSet);
    // when storage is ready, request all collections it stores
    // if nothing provided
      // create at least one collection
    // wrap those elements into `insert` command
    // supply insert command with appropriate collection
  }

  /**
   * Get collection by id
   * @throws NoSuchCollection
   */
  public Collection<T, E> getCollection(CollectionId id) throws NoSuchCollection {
    // if no collection specified, return default collection
    return getCollectionAndStorage(id).getValue1();
    
  }

  private Pair<IStorage<Collection<T, E>>, Collection<T, E>> getCollectionAndStorage(CollectionId collectionId) throws NoSuchCollection {
    for (var entry : storageToCollectionMap.entrySet()) {
      for (var collection : entry.getValue()) {
        if (collection.getMetadataSnapshot().getId().equals(collectionId)) {
          return new Pair<IStorage<Collection<T, E>>,Collection<T,E>>(entry.getKey(), collection);
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

    Observable.just(collection).subscribe(storage);
  }
}
