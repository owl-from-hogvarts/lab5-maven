package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

/**
 * Trivial command receiver.
 * It does absolutely nothing except just passing calls to underling collection
 */
@NonNullByDefault
public class CollectionCommandReceiver<P, E extends IWithId<? extends BaseId>, M extends IWithId<? extends BaseId>> implements ICollection<P, E, M> {
  private final ICollection<P, E, M> collection;

  public CollectionCommandReceiver(ICollection<P, E, M> collection) {
    this.collection = collection;
  }

  @Override
  public void insert(P prototype) throws ValidationError {
    this.collection.insert(prototype);
  }

  @Override
  public void replace(ISerializableKey key, P prototype) {
    this.collection.replace(key, prototype);
  }

  @Override
  public void delete(ISerializableKey key) {
    this.collection.delete(key);
  }

  @Override
  public Observable<E> getEvery$() {
    return this.collection.getEvery$();
  }

  @Override
  public Observable<Entry<ISerializableKey, E>> getEveryWithKey$() {
    return this.collection.getEveryWithKey$();
  }

  @Override
  public Observable<E> getById$(UUID_ElementId id) {
    return this.collection.getById$(id);
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
  
}
