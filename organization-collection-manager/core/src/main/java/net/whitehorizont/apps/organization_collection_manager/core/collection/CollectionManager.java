package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

/**
 * Looks up storage for collection.
 * 
 * 
 * Provides abstraction over available stores
 * passes on operations on collections to appropriate storage.
 */
@NonNullByDefault
public class CollectionManager<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>> implements ICollectionManager<C, M> {
  private Map<IBaseStorage<C, M>, Set<C>> storageAssociations = new HashMap<>();

  // makes collections in source available for loading
  
  public void addStorage(IBaseStorage<C, M> storage) {
    // create appropriate mapping for storage
    storageAssociations.put(storage, new HashSet<>());
  }

  
  public Observable<C> getCollection() throws StorageInaccessibleError {
    return getBestStorage().flatMap(storage -> storage.load());
  }

  /**
   * Get collection by id
   * 
   * @throws CollectionNotFound
   */
  
  public C getCollection(BaseId id) throws CollectionNotFound {
    return getCollectionAndStorage(id).blockingGet().getValue1();
  }

  private Observable<IBaseStorage<C, M>> getBestStorage() throws StorageInaccessibleError {
    for (final var store : storageAssociations.entrySet()) {
      final var storage = store.getKey();
      return Observable.just(storage);
    }

    // TODO: think up better error type; something like NoStoragesAvailable
    throw new StorageInaccessibleError();
  }

  private Single<Pair<IBaseStorage<C, M>, C>> getCollectionAndStorage(BaseId collectionId) throws CollectionNotFound {
    for (final var store : storageAssociations.entrySet()) {
      assert store != null;
      final var storage = store.getKey();

      try {
        final var collection = openCollectionSafe(store, collectionId);
        return Single.just(new Pair<>(storage, collection));

      } catch (CollectionNotFound e) {
        continue;
      }
    }

    throw new CollectionNotFound();

    // TODO: handle collection id collisions (when same collection somehow stored in
    // multiple storages)
  }

  /**
   * Opens collection. If collection is already opened, returns that instance
   * @param storageAssociation
   * @param collectionId
   * @return
   * @throws CollectionNotFound
   */
  private C openCollectionSafe(Entry<IBaseStorage<C, M>, Set<C>> storageAssociation, BaseId collectionId) throws CollectionNotFound {
    final var openedCollectionMaybe = storageAssociation.getValue().stream()
        .filter(collection -> collection.getMetadataSnapshot().getId().equals(collectionId)).findAny();

    if (openedCollectionMaybe.isEmpty()) {
      return openCollection(storageAssociation.getKey(), collectionId).blockingFirst();
    }
    
    final var collection = openedCollectionMaybe.get();
    return collection;

  }

  private Observable<C> openCollection(IBaseStorage<C, M> storage, BaseId collectionId) throws CollectionNotFound {
    return storage.load(collectionId);
  }

  /**
   * Saves collection by collection id
   * 
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError
   */
  
  public void save(BaseId collectionId) throws CollectionNotFound, StorageInaccessibleError {
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

  
  public C getCollectionSafe(M metadata) throws StorageInaccessibleError {
    try {
      return getCollection(metadata.getId());
    } catch (CollectionNotFound e) {
      final Observable<C> newCollection = getBestStorage().flatMap(storage -> storage.loadSafe(metadata));
      return newCollection.blockingFirst();
    }
  }
}
