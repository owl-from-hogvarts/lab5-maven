package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IStorage;

/**
 * Maps storages to collections they hold.
 * 
 */
public class CollectionManager<T, E> {
  private Map<IStorage, Set<Collection<T, E>>> storageToCollectionMap;

  public void addStorage(IStorage storage, DataSinkSource<T, E> dataSink) {
    // create appropriate mapping for storage
    final var collectionSet = new HashSet<Collection<T, E>>();
    storageToCollectionMap.put(storage, collectionSet);
    // when storage is ready, request all collections it stores
    // if nothing provided
      // create at least one collection
    // wrap those elements into `insert` command
    // supply insert command with appropriate collection
  }

  public void getCollection() {
    // if no collection specified, return default collection
  }

  public void save() {
    IStorage testStorage;
    CollectionId id;
    final var foundCollection = this.findById(storageToCollectionMap.get(testStorage).parallelStream(), id);
    Observable.just(foundCollection).subscribe(testStorage);
  }

  private Collection<T, E> findById(Stream<Collection<T, E>> stream, CollectionId id) {
    final var foundCollection = stream.filter((collection) -> collection.getMetadataSnapshot().getId().equals(id)).findFirst().get();
    assert foundCollection != null;
    return foundCollection;
  }
}
