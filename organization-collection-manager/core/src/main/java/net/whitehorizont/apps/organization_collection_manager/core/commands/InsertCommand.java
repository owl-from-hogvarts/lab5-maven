package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;

@NonNullByDefault
public class InsertCommand<P, C extends ICollection<P, ?, ?>> implements ICommand<Void> {
  private final P prototype;
  private final C collection;

  public InsertCommand(P prototype, C collection) {
    this.prototype = prototype;
    this.collection = collection;
  }

  // @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      final var collection = this.collection;
      collection.insert(prototype);
    });
  }
  
}
