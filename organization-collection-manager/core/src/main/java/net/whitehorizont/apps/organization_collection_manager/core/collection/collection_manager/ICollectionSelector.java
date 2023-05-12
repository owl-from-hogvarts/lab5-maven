package net.whitehorizont.apps.organization_collection_manager.core.collection.collection_manager;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

@NonNullByDefault
// package private
interface ICollectionSelector<C extends ICollection<?, ?, M>, M extends IWithId<? extends BaseId>> {
    /**
   * Select collection from list of opened collections
   * 
   * ! THIS METHOD SHOULD NOT OPEN ANY NEW COLLECTIONS ! 
   * 
   * @param openedCollections
   * @return
   * @throws CollectionNotFound
   */
  Observable<C> select(IBaseStorage<C, M> storage, Iterable<C> openedCollections) throws CollectionNotFound;

}
