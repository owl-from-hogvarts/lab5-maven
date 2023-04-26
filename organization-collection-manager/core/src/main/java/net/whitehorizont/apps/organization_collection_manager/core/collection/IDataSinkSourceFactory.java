package net.whitehorizont.apps.organization_collection_manager.core.collection;

public interface IDataSinkSourceFactory<P, E, V> {
  DataSinkSource<P, E, V> getDataSinkSourceFor(V validationObject);
}
