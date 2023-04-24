package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.nio.ByteBuffer;

import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.DeserializationError;

// adapter needs not just elements but the whole collection including metadata
// adapter is built for pair of collection type and file format
// adapter provides introspection over storage
public interface IFileAdapter<C extends IBaseCollection<?, ?, ?>> {
  ByteBuffer serialize(C toSerialize);
  C deserialize(ByteBuffer fileContent) throws DeserializationError;
}
