package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.time.Instant;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_CollectionId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;

@NonNullByDefault
public class CollectionMetadata implements IWithId<UUID_CollectionId>, IFieldDefinitionNode {
  private static String TITLE = "Metadata";
  
  private final UUID_CollectionId collectionId;
  private final Instant creationTime;
  
  public Instant getCreationTime() {
    return creationTime;
  }

  public CollectionMetadata(Builder builder) {
    collectionId = builder.collectionId.orElse(new UUID_CollectionId());
    this.creationTime = builder.creationTime.orElse(Instant.now());
  }

  public UUID_CollectionId getId() {
    return collectionId;
  }

  public static class Builder {
    private Optional<UUID_CollectionId> collectionId = Optional.empty();
    private Optional<Instant> creationTime = Optional.empty();

    public Builder(UUID_CollectionId collectionId) {
      this.collectionId = Optional.of(collectionId);
    }

    public Builder creationTime(Instant creationTime) {
      this.creationTime = Optional.of(creationTime);
      return this;
    }

    public Builder() {
    }
    

  }

  @Override
  public String getDisplayedName() {
    return TITLE;
  }

  @Override
  public Iterable<FieldDefinition<?, ?>> getFields() {

  }

  @Override
  public Iterable<IFieldDefinitionNode> getChildren() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getChildren'");
  }
}
