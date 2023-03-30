package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.util.Map.Entry;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ISerializableKey;

public class StorableKeyedCollectionPrototype<E, M> implements IStorableKeyedCollection<E, M> {

  @Override
  public Observable<Entry<ISerializableKey, E>> getEveryWithKey$() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getEveryWithKey$'");
  }

  @Override
  public M getMetadataSnapshot() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getMetadataSnapshot'");
}
  
}
