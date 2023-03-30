package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.util.Map.Entry;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ISerializableKey;

/**
 * @param <E> type of elements 
 */
public interface IStorableKeyedCollection<E, M> {
  Observable<Entry<ISerializableKey, E>> getEveryWithKey$();
  M getMetadataSnapshot();
}
