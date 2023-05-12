package net.whitehorizont.apps.organization_collection_manager.core.collection.collection_manager;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

@NonNullByDefault
// single interface with two methods to emphasis their semantically interconnection
// package private
interface ICollectionSelectorOpener<C extends IBaseCollection<?, ?, M>, M extends IWithId<? extends BaseId>> extends ICollectionSelector<C, M> {
  /**
   * Called when collection could not be looped up by select
   * 
   * @param storage
   * @return
   * @throws CollectionNotFound
   */
  Observable<C> open(IBaseStorage<C, M> storage);
}
