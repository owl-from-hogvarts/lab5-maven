package net.whitehorizont.apps.organization_collection_manager.core.storage;

import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.DeserializationError;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

// adapter needs not just elements but the whole collection including metadata
// adapter is built for pair of collection type and file format
// adapter provides introspection over storage
public interface IFileAdapter<C extends IBaseCollection<?, ?, M>, M extends IWithId<? extends BaseId>> {
  byte[] serialize(C toSerialize);
  C deserialize(byte[] fileContent) throws DeserializationError, ValidationError, ResourceEmpty;
  C fromMetadata(M metadata);
}
