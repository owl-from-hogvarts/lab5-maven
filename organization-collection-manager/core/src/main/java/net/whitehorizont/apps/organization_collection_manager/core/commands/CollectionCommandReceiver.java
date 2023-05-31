package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.DuplicateElements;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.NoSuchElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Trivial command receiver.
 * It does absolutely nothing except just passing calls to underling collection
 */
@NonNullByDefault
public class CollectionCommandReceiver<P extends IElementPrototype<?>, E extends ICollectionElement<P>, M extends IWithId<? extends BaseId>> implements ICollection<P, E, M> {
  private final ICollection<P, E, M> collection;

  public CollectionCommandReceiver(ICollection<P, E, M> collection) {
    this.collection = collection;
  }

  @Override
  public void insert(P prototype) throws ValidationError, DuplicateElements {
    this.collection.insert(prototype);
  }

  @Override
  public void replace(BaseId key, P prototype) throws ValidationError, NoSuchElement {
    this.collection.replace(key, prototype);
  }

  public Observable<Void> replace(BaseId id, IPrototypeCallback<P> callback) {
    final var entries = this.collection.getEveryWithKey$()
    .filter(keyElement -> keyElement.getValue().getId().equals(id));
    final var amount = entries.count().blockingGet();
    if (amount < 1) {
      return Observable.error(new NoSuchElement(id));
    }

    return entries.flatMap(keyElement -> {
      final var prototype = keyElement.getValue().getPrototype();
      try {
        final var updatedPrototype = callback.apply(prototype);
        final var key = keyElement.getKey();
        this.collection.replace(key, updatedPrototype);
        return Observable.empty();
      } catch (ValidationError | NoSuchElement e) {
        return Observable.error(e);
      }
    });
  }

  public static interface IPrototypeCallback<P extends IElementPrototype<?>> {
    P apply(P prototype) throws ValidationError;
  }

  @Override
  public E delete(BaseId key) throws NoSuchElement {
    return this.collection.delete(key);
  }

  @Override
  public Observable<E> getEvery$() {
    return this.collection.getEvery$();
  }

  @Override
  public Observable<Entry<BaseId, E>> getEveryWithKey$() {
    return this.collection.getEveryWithKey$();
  }

  @Override
  public Observable<List<E>> getAll$() {
    return this.collection.getAll$();
  }

  @Override
  public M getMetadataSnapshot() {
    return this.collection.getMetadataSnapshot();
  }

  @Override
  public void clear() {
    this.collection.clear();
  }

  @Override
  public P getElementPrototype() {
    return this.collection.getElementPrototype();
  }

  @Override
  public BaseId getElementIdFromString(String idString) throws ValidationError {
    return this.collection.getElementIdFromString(idString);
  }

  @Override
  public BaseId getElementKeyFromString(String keyString) throws ValidationError {
    return this.collection.getElementKeyFromString(keyString);
  }

  public void insert(String keyString, P prototype) throws ValidationError, DuplicateElements {
    final var key = this.collection.getElementKeyFromString(keyString);
    this.collection.insert(key, prototype);
  }

  @Override
  public void insert(BaseId key, P prototype) throws ValidationError, DuplicateElements {
    this.collection.insert(key, prototype);
  }
  
}
