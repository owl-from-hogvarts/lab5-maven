package net.whitehorizont.apps.organization_collection_manager.core.storage;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.DuplicateElements;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.NoSuchElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.DeserializationError;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

// adapter needs not just elements but the whole collection including metadata
// adapter is built for pair of collection type and file format
// adapter provides introspection over storage
@NonNullByDefault
public interface IFileAdapter<C extends ICollection<?, ?, M>, @NonNull M extends IWithId<? extends BaseId>> {
  byte[] serialize(C toSerialize);
  C deserialize(byte[] fileContent) throws DeserializationError, ValidationError, ResourceEmpty, NoSuchElement, KeyGenerationError, DuplicateElements;
  /** 
   * If storage is empty or does not contain desired collection, 
   * this should return new empty collection with provided or default metadata
   * 
   */
  C deserializeSafe();
  C deserializeSafe(M metadata);
}
