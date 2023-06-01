package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.thoughtworks.xstream.XStream;
import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.DuplicateElements;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IFileAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class CollectionAdapter<R, P extends IElementPrototype<R>, E extends ICollectionElement<P>, F extends IElementFactory<P, E, ICollection<P, E, ?>, ?>>
    implements IFileAdapter<ICollection<P, E, CollectionMetadata>, CollectionMetadata> {
  private final XStream serializer = new XStream();
  {
    serializer.allowTypesByWildcard(new String[]{"net.whitehorizont.apps.organization_collection_manager.core.**"});
    serializer.processAnnotations(StorageXml.class);
  }
  private final F elementFactory;
  @SuppressWarnings("null")
  private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

  public CollectionAdapter(F dataSinkSourceFactory) {
    this.elementFactory = dataSinkSourceFactory;
  }

  private CollectionXml<R, CollectionMetadata> prepareCollection(ICollection<P, E, CollectionMetadata> collection) {    
    final var elements = collection.getEvery$()
        .map(element -> element.getPrototype().getRawElementData())
        .toList().blockingGet();
    return new CollectionXml<>(collection.getMetadataSnapshot(), elements);
  }

  @Override
  public byte[] serialize(ICollection<P, E, CollectionMetadata> toSerialize) {
    final var collectionXml = prepareCollection(toSerialize);

    final String collectionXmlSerialized = serializer.toXML(collectionXml);
    // algorithm hardcoded for now
    final var storageIntegrity = new FileIntegrity(new Sha256(), collectionXmlSerialized.getBytes(DEFAULT_ENCODING));
    final var storageMetadata = new FileMetadataXml(storageIntegrity);

    final var storageXml = new StorageXml<>(collectionXml, storageMetadata);

    // hard code UTF-8 cause who the fuck except shitty windows uses other encoding for files?
    // any element of array should never be null
    @SuppressWarnings("null")
    final byte @NonNull[] serializedContent = serializer.toXML(storageXml).getBytes(DEFAULT_ENCODING);
    return serializedContent;
  }

  public RamCollection<P, E> parse(ByteBuffer fileContent) throws ValidationError, KeyGenerationError, DuplicateElements {
    // receive buffer
    // cast to string
    final String xml_content = DEFAULT_ENCODING.decode(fileContent).toString();
    // parse xml:
    @SuppressWarnings("unchecked")
    StorageXml<R, CollectionMetadata> storageXmlRepresentation = (StorageXml<R, CollectionMetadata>) serializer.fromXML(xml_content);

    final CollectionMetadata collectionMetadata = storageXmlRepresentation.collection.metadata;
    
    final var collection = new RamCollection<P, E>(this.elementFactory, collectionMetadata);

    
    for (var elementXmlRepresentation : storageXmlRepresentation.collection.elements) {
      final var prototype = this.elementFactory.getElementPrototype();
      prototype.setFromRawData(elementXmlRepresentation);

      collection.insert(prototype);
    }

    return collection;
    //! reliable integrity check is impossible to implement without custom XML parser 
  }

  @Override
  public RamCollection<P, E> deserialize(byte[] fileContent) throws ValidationError, ResourceEmpty, KeyGenerationError, DuplicateElements {
    if (fileContent.length == 0) {
      // if something wrong with file, error any way
      // this is responsibility of client code what to with errors
      // TODO: check for file signature (though xstream should throw validation error)
      throw new ResourceEmpty();
    }

    final var fileContentBuffer = ByteBuffer.wrap(fileContent);
    assert fileContentBuffer != null;

    return parse(fileContentBuffer);
  }

  @Override
  public RamCollection<P, @NonNull E> deserializeSafe(CollectionMetadata metadata) {
    return new RamCollection<>(elementFactory, metadata);
  }

  @Override
  public RamCollection<P, E> deserializeSafe() {
    final var emptyCollectionMetadata = new CollectionMetadata(new Builder());
    return new RamCollection<>(elementFactory, emptyCollectionMetadata);
  }
}
