package net.whitehorizont.apps.collection_manager.core.collection;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_CollectionId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class CollectionMetadataDefinition {
  public static String TITLE = "Metadata";

  private static FieldMetadataExtended<CollectionMetadataComputed, CollectionMetadataComputed, Instant> CREATION_TIME_METADATA = FieldMetadataExtended
      .<CollectionMetadataComputed, CollectionMetadataComputed, Instant>builder()
      .setDisplayedName("Creation time")
      .setValueGetter(metadata -> metadata.creationTime)
      .setNullable()
      .build();
  private static FieldMetadataExtended<CollectionMetadataComputed, CollectionMetadataComputed, UUID_CollectionId> COLLECTION_ID_METADATA = FieldMetadataExtended
      .<CollectionMetadataComputed, CollectionMetadataComputed, UUID_CollectionId>builder()
      .setDisplayedName("Collection ID")
      .setValueGetter(metadata -> metadata.collectionId)
      .build();

  private static FieldMetadataExtended<CollectionMetadataComputed, CollectionMetadataComputed, String> COLLECTION_TYPE_METADATA = FieldMetadataExtended
      .<CollectionMetadataComputed, CollectionMetadataComputed, String>builder()
      .setDisplayedName("Type of collection")
      .setValueGetter(metadata -> metadata.collectionType)
      .build();

  private static FieldMetadataExtended<CollectionMetadataComputed, CollectionMetadataComputed, Integer> ELEMENT_COUNT_METADATA = FieldMetadataExtended
      .<CollectionMetadataComputed, CollectionMetadataComputed, Integer>builder()
      .setDisplayedName("Element count")
      .setValueGetter(metadata -> metadata.elementCount)
      .build();

  public static class CollectionMetadata implements IWithId<UUID_CollectionId>, IWritableHostFactory<CollectionMetadataComputed> {
    // protected 'cause don't fuck around java generics
    protected UUID_CollectionId collectionId;
    protected Instant creationTime = Instant.now();

    private CollectionMetadata(CollectionMetadata metadata) {
      this(metadata.collectionId);
      this.creationTime = metadata.creationTime;

    }

    public CollectionMetadata() {
      this(new UUID_CollectionId());
    }

    public CollectionMetadata(UUID_CollectionId collectionId) {
      this.collectionId = collectionId;
    }

    public CollectionMetadata setCreationTime(Instant creationTime) {
      this.creationTime = creationTime;
      return this;
    }

    @Override
    public UUID_CollectionId getId() {
      return this.collectionId;
    }

    @Override
    public CollectionMetadataComputed createWritable() {
      return new CollectionMetadataComputed(this);
    }
  }

  // some fields are computed so moving them into separate class
  // this allows to not store them
  public static class CollectionMetadataComputed extends CollectionMetadata implements Serializable {
    // temp values to catch uninitialized fields
    private int elementCount = -789;
    private String collectionType = "foobar";


    private CollectionMetadataComputed(CollectionMetadata metadata) {
      super(metadata);
    }

    public void setElementCount(int elementCount) {
      this.elementCount = elementCount;
    }

    public void setCollectionType(String collectionType) {
      this.collectionType = collectionType;
    }
  }

  // public MetadataComposite<Object, CollectionMetadata, CollectionMetadata> getTree() {
  //   final List<FieldMetadataExtended<CollectionMetadata, CollectionMetadata, ?>> leafs = new ArrayList<>();
  //   leafs.add(CREATION_TIME_METADATA);
  //   return new MetadataComposite<Object, CollectionMetadata, CollectionMetadata>(TITLE, leafs, new ArrayList<>(), null);
  // }

  public static MetadataComposite<Object, CollectionMetadataComputed, CollectionMetadataComputed> getMetadata() {
    final List<FieldMetadataExtended<CollectionMetadataComputed, CollectionMetadataComputed, ?>> leafs = new ArrayList<>();
    leafs.add(COLLECTION_ID_METADATA);
    leafs.add(CREATION_TIME_METADATA);
    leafs.add(COLLECTION_TYPE_METADATA);
    leafs.add(ELEMENT_COUNT_METADATA);


    return new MetadataComposite<Object, CollectionMetadataComputed, CollectionMetadataComputed>(TITLE, leafs, new ArrayList<>(), null);
  }
}
