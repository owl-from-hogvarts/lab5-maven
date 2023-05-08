package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
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
public class CollectionManager<@NonNull C extends IBaseCollection<?, ?, @NonNull ?>> {
  private Map<IBaseStorage<C, BaseId, ?>, Set<C>> storageAssociations;

  // makes collections available for loading
  public void addStorage(IBaseStorage<C, BaseId, ?> storage) {
    // create appropriate mapping for storage
    storageAssociations.put(storage, new HashSet<>());
  }

  public Observable<C> getCollection() {
    return getDefaultCollections();
  }

  /**
   * Get collection by id
   * 
   * @throws CollectionNotFound
   */
  public C getCollection(BaseId id) throws CollectionNotFound {
    // if no collection specified, return default collection
    return getCollectionAndStorage(id).blockingGet().getValue1();
  }

  private Observable<C> getDefaultCollections() {
    final List<Observable<C>> defaultCollections = new ArrayList<>();
    
    for (final var store : storageAssociations.entrySet()) {
      final var storage =  store.getKey();
      final var collection = storage.load();

      defaultCollections.add(collection);
    }
    
    return Observable.concat(defaultCollections);
  }

  private Single<Pair<IBaseStorage<C, BaseId, ?>, C>> getCollectionAndStorage(BaseId collectionId) throws CollectionNotFound {
    for (var store : storageAssociations.entrySet()) {
      try {
        final var collection = getOpenedCollection(store, collectionId);
        return Single.just(new Pair<>(store.getKey(), collection));
      } catch (CollectionNotFound e) {
        try {
          store.getKey().load(collectionId);
        } catch (CollectionNotFound a) {
          continue;
        }
        continue;
      }
    }

    throw new CollectionNotFound();

    // TODO: handle collection id collisions (when same collection somehow stored in
    // multiple storages)
  }

  private C getOpenedCollection(Entry<IBaseStorage<C, BaseId, ?>, Set<C>> storageAssociation, BaseId collectionId) throws CollectionNotFound {
    final var collectionMaybe = storageAssociation.getValue().stream()
        .filter(collection -> collection.getMetadataSnapshot().getId().equals(collectionId)).findAny();

    if (collectionMaybe.isPresent()) {
      final var collection = collectionMaybe.get();
      return collection;
    }

    throw new CollectionNotFound();
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
}
