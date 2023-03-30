package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

public interface IDataSink<T> {
  void supply(T prototype) throws ValidationError;
  void supply(T prototype, boolean force) throws ValidationError;
}
