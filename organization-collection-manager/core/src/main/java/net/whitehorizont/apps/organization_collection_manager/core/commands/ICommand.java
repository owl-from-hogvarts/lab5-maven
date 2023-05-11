package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;

@NonNullByDefault
public interface ICommand<@NonNull T, C extends IBaseCollection<?,?,?>> extends IExecutable<T> {
  void setCollection(Observable<C> collection);
  boolean hasPreferredCollection();
}
