package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

@NonNullByDefault
public interface ICommandQueue {

  <@NonNull T> Observable<T> push(ICommand<T> command);

  void executeNext(ConnectableObservable<?> command);

}