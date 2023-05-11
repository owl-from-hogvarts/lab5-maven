package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

/**
 * @param <P> element's prototype
 */
@NonNullByDefault
public interface IDataSink<P> {
  void supply(P prototype) throws ValidationError;
  void supply(P prototype, boolean force) throws ValidationError;
}
