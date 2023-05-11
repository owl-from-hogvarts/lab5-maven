package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;

@NonNullByDefault
public interface ICommandQueue<CM extends ICollectionManager<C, ?>, C extends IBaseCollection<?, ?, ?>> {

  <@NonNull T> Observable<T> push(BaseCommand<T, C> command);

  <@NonNull T> void push(ISystemCommand<T> command);

  void executeNext(ConnectableObservable<?> command);

}