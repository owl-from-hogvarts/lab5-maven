package net.whitehorizont.apps.organization_collection_manager.core.collection;

public interface IDataSink<T> {
  void supply(T prototype);
}
