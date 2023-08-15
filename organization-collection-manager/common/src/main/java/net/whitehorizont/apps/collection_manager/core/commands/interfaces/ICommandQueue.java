package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public interface ICommandQueue<DependencyManager> {

  <@NonNull T> Observable<T> push(ICommand<T, DependencyManager> command);

}