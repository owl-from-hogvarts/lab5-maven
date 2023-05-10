package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.libs.file_system.AssertHelpers;

/**
 * Looks up storage for collection.
 * 
 * 
 * Provides abstraction over available stores
 * passes on operations on collections to appropriate storage.
 */
@NonNullByDefault
public class CollectionManager<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>>
    implements ICollectionManager<C, M> {
  private Map<IBaseStorage<C, M>, Set<C>> storageAssociations = new HashMap<>();

  // makes collections in source available for loading

  public void addStorage(IBaseStorage<C, M> storage) {
    // create appropriate mapping for storage
    storageAssociations.put(storage, new HashSet<>());
  }

  public Observable<C> getCollection() throws StorageInaccessibleError {
    // request best storage
    // try open default collection on it
    try {
      return this.getCollectionAndStorage(new SelectBestStorage<>(), new SelectDefaultCollection<>()).toObservable()
          .map(association -> association.getValue1());
    } catch (CollectionNotFound e) {
      assert false : AssertHelpers.getAssertMessageFor("CollectionManager.java", "getCollection");
      return Observable.error(new StorageInaccessibleError());
    }
  }

  /**
   * Get collection by id
   * 
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError
   */

  public C getCollection(BaseId id) throws CollectionNotFound, StorageInaccessibleError {
    // request storage holding collection with id
    // try open collection by id on it
    return this.getCollectionAndStorage(new StandardSelectStorage<>(), new SelectCollectionById<>(id)).blockingGet()
        .getValue1();
  }

  /**
   * 
   * @param storageSelector
   * @param collectionSelector
   * @return
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError thrown when no stores have been added to
   *                                  collection manager or all added stores are
   *                                  inaccessible
   */
  // allow passing custom open functions
  private Single<Pair<IBaseStorage<C, M>, C>> getCollectionAndStorage(IStorageSelector<C, M> storageSelector,
      ICollectionSelector<C, M> collectionSelector) throws CollectionNotFound, StorageInaccessibleError {
    for (final var store : storageSelector.select(this.storageAssociations)) {
      assert store != null;
      try {
        // select collection form store
        final var storage = store.getKey();
        final var collection = collectionSelector.select(storage).blockingFirst();

        // add collection to list of opened collections so it can be saved in the future
        final var openedCollections = store.getValue();
        openedCollections.add(collection);

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
   * Saves collection by collection id
   * 
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError
   */

  public void save(BaseId collectionId) throws CollectionNotFound, StorageInaccessibleError {
    final var storageAndCollection = getCollectionAndStorage(new SelectBestStorage<>(),
        new SelectCollectionById<>(collectionId)).blockingGet();
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

  public C getCollectionSafe(M metadata) throws StorageInaccessibleError, CollectionNotFound {
    return this.getCollectionAndStorage(new SelectBestStorage<>(), new SelectCollectionByMetadata<>(metadata))
        .blockingGet().getValue1();
  }
}
