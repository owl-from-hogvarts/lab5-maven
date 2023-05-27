package net.whitehorizont.apps.organization_collection_manager.core.collection.keys;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

// guaranteed to be unique within collection
// should be granted by collection
// id is a key for collection element, not part of it
@NonNullByDefault
public class UUID_ElementId extends UUID_BaseId {
  public UUID_ElementId(String idString) throws ValidationError {
    super(idString);
  }
  
  public UUID_ElementId() {
    super();
  }
}
