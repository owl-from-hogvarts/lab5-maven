package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;

@NonNullByDefault
public class InsertCommand<E extends ICollectionElement> implements ICommand<Void> {
  private final E element;
  private final CollectionCommandReceiver<E> collection;
  private final ElementKey key;

  public InsertCommand(ElementKey key, E element, CollectionCommandReceiver<E> collectionReceiver) {
    this.element = element;
    this.collection = collectionReceiver;
    this.key = key;
  }

  @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      final var collection = this.collection;
      collection.insert(key, element);
      subscriber.onComplete();
    });
  }
  
}
