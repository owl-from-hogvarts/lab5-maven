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
    return this.getCollectionAndStorage(new StandardSelectStorage<>(), new SelectCollectionById<C, M>(id)).blockingGet()
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
  private Single<Pair<IBaseStorage<C, M>, C>> _getCollectionAndStorage(IStorageSelector<C, M> storageSelector, ICollectionSelectorWriteable<C, M> collectionSelector) throws CollectionNotFound, StorageInaccessibleError {
    for (final var store : storageSelector.select(this.storageAssociations)) {
      assert store != null;
      // select collection form store
      final var storage = store.getKey();
      final var openedCollections = store.getValue();

      try {
        final var collection = collectionSelector.select(storage, openedCollections).blockingFirst();
        return Single.just(new Pair<>(storage, collection));
      } catch (CollectionNotFound e) {
        continue;
      }
    }

    throw new CollectionNotFound();

    // TODO: handle collection id collisions (when same collection somehow stored in
    // multiple storages)
  }

  private Single<Pair<IBaseStorage<C, M>, C>> getCollectionAndStorage(IStorageSelector<C, M> storageSelector, ICollectionSelectorOpener<C, M> collectionSelector) throws CollectionNotFound, StorageInaccessibleError {
    return _getCollectionAndStorage(storageSelector, selectOrOpenCollection(collectionSelector));
  }

  private Single<Pair<IBaseStorage<C, M>, C>> getCollectionAndStorage(IStorageSelector<C, M> storageSelector, ICollectionSelector<C, M> collectionSelector) throws CollectionNotFound, StorageInaccessibleError {
    return _getCollectionAndStorage(storageSelector, selectCollection(collectionSelector));
  }


  // select and open
  private ICollectionSelectorWriteable<C, M> selectOrOpenCollection(ICollectionSelectorOpener<C, M> selector) {
    // select
    // lamba needs write access for opened collections
    return (IBaseStorage<C, M> storage, Set<C> openedCollections) -> {
      try {
        return selector.select(storage, openedCollections);
      } catch (CollectionNotFound e) {
        // if fails: open
        final var openedCollection = selector.open(storage).blockingFirst();
        openedCollections.add(openedCollection);
        // reuse received collection to prevent loading it again
        // ? may be better use subject
        return Observable.just(openedCollection);
      }
    };
  }

  private ICollectionSelectorWriteable<C, M> selectCollection(ICollectionSelector<C, M> selector) {
    return (IBaseStorage<C, M> storage, Set<C> openedCollections) -> {
      return selector.select(storage, openedCollections);
    };
  }

  /**
   * Saves collection by collection id
   * 
   * @throws CollectionNotFound
   * @throws StorageInaccessibleError
   */

  public void save(BaseId collectionId) throws CollectionNotFound, StorageInaccessibleError {
    final var storageAndCollection = getCollectionAndStorage(new StandardSelectStorage<>(),
        new SelectCollectionById<>(collectionId)).blockingGet();
    final var storage = storageAndCollection.getValue0();
    final var collection = storageAndCollection.getValue1();

    storage.save(collection);
    // new SaveCommand
    // result$ = commandQueue.add(saveCommand)
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

@NonNullByDefault
interface ICollectionSelectorWriteable<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>> {
  Observable<C> select(IBaseStorage<C, M> storage, Set<C> openedCollections) throws CollectionNotFound;
}
