package net.whitehorizont.apps.organization_collection_manager.core.collection;

import io.reactivex.rxjava3.annotations.NonNull;

public interface IDataSinkSourceFactory<P, E, V> {
  @NonNull DataSinkSource<P, E, V> getDataSinkSourceFor(V validationObject);
}
