package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_CollectionId;

@NonNullByDefault
public class CollectionMetadata implements IWithId<UUID_CollectionId> {
  private final UUID_CollectionId collectionId;
  
  public CollectionMetadata(Builder builder) {
    collectionId = builder.collectionId;
  }

  public UUID_CollectionId getId() {
    return collectionId;
  }

  public static class Builder {
    private UUID_CollectionId collectionId;

    public Builder(UUID_CollectionId collectionId) {
      this.collectionId = collectionId;
    }

    public Builder() {
      this(new UUID_CollectionId());
    }
    

  }
}
