package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.List;
import java.util.Map.Entry;

import io.reactivex.rxjava3.core.Observable;

public interface IBaseCollection<D extends IDataSink<?>, E extends IWithId<? extends BaseId>, M> {

  /**
   * Collection listens on returned sink to receive new elements
   */
  D getDataSink();

  Observable<E> getEvery$();

  Observable<Entry<ISerializableKey, E>> getEveryWithKey$();

  Observable<E> getById$(UUID_ElementId id);

  Observable<List<E>> getAll$();

  // stores creation time
  M getMetadataSnapshot();

  void clear();

}