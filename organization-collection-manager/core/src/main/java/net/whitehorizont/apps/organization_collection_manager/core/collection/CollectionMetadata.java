package net.whitehorizont.apps.organization_collection_manager.core.collection;

public class CollectionMetadata {
  private final CollectionId collectionId;
  
  public CollectionMetadata(Builder builder) {
    collectionId = builder.collectionId;
  }

  public CollectionId getId() {
    return collectionId;
  }

  public static class Builder {
    private CollectionId collectionId;

    public Builder(CollectionId collectionId) {
      this.collectionId = collectionId;
    }
    

  }
}
