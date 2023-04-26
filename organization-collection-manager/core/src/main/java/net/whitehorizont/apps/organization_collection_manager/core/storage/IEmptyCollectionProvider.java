package net.whitehorizont.apps.organization_collection_manager.core.storage;

import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IWithId;

public interface IEmptyCollectionProvider<C extends IBaseCollection<?, ?, M>, M extends IWithId<? extends BaseId>> {
  C fromMetadata(M metadata);
}
