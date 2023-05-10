package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;

@NonNullByDefault
public class SelectDefaultCollection<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>> implements ICollectionSelector<C, M> {

  @Override
  public Observable<C> select(IBaseStorage<C, M> storage) {
    return storage.load();
  }
  
}
