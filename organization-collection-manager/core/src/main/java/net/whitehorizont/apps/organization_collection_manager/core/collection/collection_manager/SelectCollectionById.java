package net.whitehorizont.apps.organization_collection_manager.core.collection.collection_manager;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

@NonNullByDefault
class SelectCollectionById<C extends IBaseCollection<?, ?, M>, M extends IWithId<? extends BaseId>> implements ICollectionSelector<C, M> {
  private final BaseId id;

  SelectCollectionById(BaseId id) {
    this.id = id;
  }

  @Override
  public Observable<C> select(IBaseStorage<C, M> storage, Iterable<C> openedCollections) throws CollectionNotFound {
    final var collectionMaybe = Observable.fromIterable(openedCollections).blockingStream().filter(collection -> collection.getMetadataSnapshot().getId().equals(this.id)).findAny();
    if (collectionMaybe.isEmpty()) {
      throw new CollectionNotFound();
    }

    return Observable.just(collectionMaybe.get());
  }

  
}
