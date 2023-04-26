package net.whitehorizont.apps.organization_collection_manager.core.collection;

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
    

  }
}
