package net.whitehorizont.apps.organization_collection_manager.core.collection.collection_manager;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

@NonNullByDefault
class SelectCollectionByMetadata<C extends IBaseCollection<?, ?, M>, M extends IWithId<? extends BaseId>> implements ICollectionSelectorOpener<C, M> {
  private final M metadata;


  public SelectCollectionByMetadata(M metadata) {
    this.metadata = metadata;
  }


  @Override
  public Observable<C> open(IBaseStorage<C, M> storage) {
    return storage.loadSafe(metadata);
  }


  @Override
  public Observable<C> select(IBaseStorage<C, M> storage, Iterable<C> openedCollections) throws CollectionNotFound {
    final var collectionMaybe = Observable.fromIterable(openedCollections).blockingStream().filter(collection -> collection.getMetadataSnapshot().getId().equals(this.metadata.getId())).findAny();
    if (collectionMaybe.isEmpty()) {
      throw new CollectionNotFound();
    }

    return Observable.just(collectionMaybe.get());
  }
  
}
