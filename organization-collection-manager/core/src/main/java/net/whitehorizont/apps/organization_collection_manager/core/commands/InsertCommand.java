package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;

@NonNullByDefault
public class InsertCommand<P extends IElementPrototype<?>> implements ICommand<Void> {
  private final P prototype;
  private final CollectionCommandReceiver<P, ?> collection;
  private final ElementKey key;

  public InsertCommand(ElementKey key, P prototype, CollectionCommandReceiver<P, ?> collectionReceiver) {
    this.prototype = prototype;
    this.collection = collectionReceiver;
    this.key = key;
  }

  @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      final var collection = this.collection;
      collection.insert(key, prototype);
      subscriber.onComplete();
    });
  }
  
}
