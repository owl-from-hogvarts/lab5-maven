package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.UUID;

// guaranteed to be unique within collection
// should be granted by collection
// id is a key for collection element, not part of it
public class ElementId implements ISerializableKey {
  private final UUID ID = UUID.randomUUID();

  @Override
  public String serialize() {
    return ID.toString();
  }
}
