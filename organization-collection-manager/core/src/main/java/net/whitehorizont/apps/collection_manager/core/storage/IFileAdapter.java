package net.whitehorizont.apps.collection_manager.core.storage;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.collection_manager.core.storage.errors.DeserializationError;
import net.whitehorizont.apps.collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

// adapter needs not just elements but the whole collection including metadata
// adapter is built for pair of collection type and file format
// adapter provides introspection over storage
@NonNullByDefault
public interface IFileAdapter<C extends ICollection<?>> {
  byte[] serialize(C toSerialize);
  C deserialize(byte[] fileContent) throws DeserializationError, ValidationError, ResourceEmpty, NoSuchElement, KeyGenerationError, DuplicateElements, StorageInaccessibleError;
  /** 
   * If storage is empty or does not contain desired collection, 
   * this should return new empty collection with provided or default metadata
   * 
   */
  C deserializeSafe();
  C deserializeSafe(CollectionMetadata metadata);
}
