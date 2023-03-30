package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.nio.ByteBuffer;

// adapter needs not just elements but the whole collection including metadata
// it also responsible for providing integrity info for file
// so adapter is built for pair of collection type and format
public interface IFileAdapter<E, M> {
  ByteBuffer serialize(IStorableKeyedCollection<E, M> toSerialize);
  IStorableKeyedCollection<E, M> deserialize(ByteBuffer fileContent);
}
