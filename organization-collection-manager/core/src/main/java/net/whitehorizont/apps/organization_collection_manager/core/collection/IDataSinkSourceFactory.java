package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IDataSinkSourceFactory<P extends IElementPrototype, E, V> {
  DataSinkSource<P, E, V> getDataSinkSourceFor(V validationObject);
}
