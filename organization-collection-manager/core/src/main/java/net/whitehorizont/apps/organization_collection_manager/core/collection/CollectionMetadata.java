package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_CollectionId;
import net.whitehorizont.apps.organization_collection_manager.lib.BasicFieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;

@NonNullByDefault
public class CollectionMetadata implements IWithId<UUID_CollectionId> {
  private static String TITLE = "Metadata";

  private static BasicFieldMetadata CREATION_TIME_METADATA = new BasicFieldMetadata("Creation time");

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
  }

  public TitledNode<ReadonlyField<?, BasicFieldMetadata>> getTree() {
    final List<ReadonlyField<?, BasicFieldMetadata>> leafs = new ArrayList<>();
    leafs.add(new ReadonlyField<Instant,BasicFieldMetadata>(CREATION_TIME_METADATA, creationTime));
    return new TitledNode<>(TITLE, leafs, new ArrayList<>());
  }
}
