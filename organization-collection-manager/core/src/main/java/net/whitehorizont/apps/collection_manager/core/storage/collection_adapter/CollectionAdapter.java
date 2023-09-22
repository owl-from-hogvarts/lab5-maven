package net.whitehorizont.apps.collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.thoughtworks.xstream.XStream;
import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.collection_manager.core.storage.IFileAdapter;
import net.whitehorizont.apps.collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Serializes {@code RamCollection} to XML
 */
@NonNullByDefault
public class CollectionAdapter<E extends ICollectionElement<E>>
    implements IFileAdapter<ICollection<E>> {

  private final XStream serializer = new XStream();
  private final RamCollection.Configuration<E, ?> collectionFactory;

  {
    serializer.allowTypesByWildcard(new String[] { "net.whitehorizont.apps.collection_manager.core.**" });
    serializer.allowTypesByWildcard(new String[] { "net.whitehorizont.apps.collection_manager.organisation.**" });
    serializer.processAnnotations(StorageXml.class);
  }

  public CollectionAdapter(RamCollection.Configuration<E, ?> collectionFactory) {
    this.collectionFactory = collectionFactory.clone();
  }

  @SuppressWarnings("null")
  private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

  private CollectionXml<E, CollectionMetadata> prepareCollection(ICollection<E> collection) {
    final var elements = collection.getEvery$()
        .toList().blockingGet();
    return new CollectionXml<>(collection.getPersistentMetadata(), elements);
  }

  @Override
  public byte[] serialize(ICollection<E> toSerialize) {
    final var collectionXml = prepareCollection(toSerialize);

    final String collectionXmlSerialized = serializer.toXML(collectionXml);
    // algorithm hardcoded for now
    final var storageIntegrity = new FileIntegrity(new Sha256(), collectionXmlSerialized.getBytes(DEFAULT_ENCODING));
    final var storageMetadata = new FileMetadataXml(storageIntegrity);

    final var storageXml = new StorageXml<>(collectionXml, storageMetadata);

    // hard code UTF-8 cause who the fuck except shitty windows uses other encoding
    // for files?
    // no array element should every be null
    @SuppressWarnings("null")
    final byte @NonNull [] serializedContent = serializer.toXML(storageXml).getBytes(DEFAULT_ENCODING);
    return serializedContent;
  }

  public RamCollection<E> parse(ByteBuffer fileContent) throws ValidationError, KeyGenerationError, DuplicateElements, StorageInaccessibleError {
    // receive buffer
    // cast to string
    final String xml_content = DEFAULT_ENCODING.decode(fileContent).toString();
    // parse xml:
    @SuppressWarnings("unchecked")
    StorageXml<E, CollectionMetadata> storageXmlRepresentation = (StorageXml<E, CollectionMetadata>) serializer
        .fromXML(xml_content);

    // DONE: move configuration to collection factory
    final CollectionMetadata collectionMetadata = storageXmlRepresentation.collection.metadata;

    final var collection = collectionFactory.build(Optional.of(collectionMetadata));
    ;

    for (var elementXmlRepresentation : storageXmlRepresentation.collection.elements) {
      collection.insert(elementXmlRepresentation);
    }

    return collection;
    // reliable integrity check is impossible to 
    // implement without custom XML parser
  }

  @Override
  public RamCollection<E> deserialize(byte[] fileContent)
      throws ValidationError, ResourceEmpty, KeyGenerationError, DuplicateElements, StorageInaccessibleError {
    if (fileContent.length == 0) {
      // if something wrong with file, error any way
      // this is responsibility of client code to decide what to do with errors
      // TODO: check for file signature (though xstream should throw validation error)
      throw new ResourceEmpty();
    }

    final var fileContentBuffer = ByteBuffer.wrap(fileContent);
    assert fileContentBuffer != null;

    return parse(fileContentBuffer);
  }

  @Override
  public RamCollection<E> deserializeSafe(CollectionMetadata metadata) {
    return this.collectionFactory.build(Optional.of(metadata));
  }

  @Override
  public RamCollection<E> deserializeSafe() {
    return this.collectionFactory.build();
  }
}
