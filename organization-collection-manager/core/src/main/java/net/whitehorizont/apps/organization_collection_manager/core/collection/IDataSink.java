package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

/**
 * @param <T> element's prototype
 */
@NonNullByDefault
public interface IDataSink<T> {
  void supply(T prototype) throws ValidationError;
  void supply(T prototype, boolean force) throws ValidationError;
}
