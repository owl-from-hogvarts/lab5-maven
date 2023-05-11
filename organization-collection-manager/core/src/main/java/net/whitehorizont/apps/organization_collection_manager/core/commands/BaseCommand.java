package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;

@NonNullByDefault
public abstract class BaseCommand<T, C extends IBaseCollection<?,?,?>>{
  private @Nullable C collection;

  final public boolean hasPreferredCollection() {
    return collection != null;
  }

  final public void setCollection(Observable<C> collection) {
    this.collection = collection.blockingFirst();
  }

  final protected @Nullable C getCollection() {
    return this.collection;
  }
  
}
