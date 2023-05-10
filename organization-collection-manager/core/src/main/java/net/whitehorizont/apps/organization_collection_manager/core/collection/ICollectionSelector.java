package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.CollectionNotFound;

@NonNullByDefault
@FunctionalInterface
// package private
interface ICollectionSelector<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>> {
  Observable<C> select(IBaseStorage<C, M> storage) throws CollectionNotFound;
}
