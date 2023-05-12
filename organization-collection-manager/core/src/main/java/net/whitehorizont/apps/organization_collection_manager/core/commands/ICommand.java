package net.whitehorizont.apps.organization_collection_manager.core.commands;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public interface ICommand<@NonNull T> {
  Observable<T> execute();
}
