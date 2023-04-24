package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;

/**
 * Looks up storage for collection.
 * 
 * 
 * Provides abstraction over available stores
 * passes on operations on collections to appropriate storage.
 */
public class CollectionManager<C extends IBaseCollection<?, ?, ?>, S extends IBaseStorage<C, K, ?>, K extends BaseId> {
  private Map<S, Set<C>> storageToCollectionMap;

  // makes collections available for loading
  public Observable<C> addStorage(S storage, DataSinkSource<?, ?, C> dataSink) {
    // create appropriate mapping for storage
    final var collectionSet = new HashSet<C>();
    storageToCollectionMap.put(storage, collectionSet);

    final var defaultCollection$ = storage.load();
    defaultCollection$.doOnNext((defaultCollection) -> collectionSet.add(defaultCollection));

    return defaultCollection$;
  }

  /**
   * Get collection by id
   * @throws NoSuchCollection
   */
  public C getCollection(K id) throws NoSuchCollection {
    // if no collection specified, return default collection
    return getCollectionAndStorage(id).getValue1();
    
  }

  private Pair<S, C> getCollectionAndStorage(K collectionId) throws NoSuchCollection {
    for (var entry : storageToCollectionMap.entrySet()) {
      for (var collection : entry.getValue()) {
        if (collection.getMetadataSnapshot().getId().equals(collectionId)) {
          return new Pair<S,C>(entry.getKey(), collection);
        }
      }
    }

    throw new NoSuchCollection();
  }

  /**
   * Saves collection by collection id
   * @throws NoSuchCollection
   */
  public void save(K collectionId) throws NoSuchCollection {
    final var storageAndCollection = getCollectionAndStorage(collectionId);
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
