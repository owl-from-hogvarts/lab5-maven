package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class InsertCommand<P> implements ICommand<Void> {
  private final P prototype;
  private final CollectionCommandReceiver<P, ?, ?> collection;

  public InsertCommand(P prototype, CollectionCommandReceiver<P, ?, ?> collectionReceiver) {
    this.prototype = prototype;
    this.collection = collectionReceiver;
  }

  // @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      final var collection = this.collection;
      collection.insert(prototype);
    });
  }
  
}
