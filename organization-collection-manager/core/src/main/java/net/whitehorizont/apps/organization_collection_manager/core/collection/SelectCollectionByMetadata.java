package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

@NonNullByDefault
public class SelectCollectionByMetadata<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>> implements ICollectionSelector<C, M> {
  private final M metadata;


  public SelectCollectionByMetadata(M metadata) {
    this.metadata = metadata;
  }


  @Override
  public Observable<C> select(IBaseStorage<C, M> storage) throws CollectionNotFound {
    return storage.loadSafe(metadata);
  }
  
}
