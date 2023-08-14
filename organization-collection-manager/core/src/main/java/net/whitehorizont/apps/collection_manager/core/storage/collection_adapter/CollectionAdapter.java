package net.whitehorizont.apps.collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
import net.whitehorizont.apps.organization_collection_manager.lib.ICanRichValidate;
import net.whitehorizont.apps.organization_collection_manager.lib.IElementInfoProvider;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Serializes {@code RamCollection} to XML
 */
@NonNullByDefault
public class CollectionAdapter<E extends ICollectionElement<E>>
    implements IFileAdapter<ICollection<E>> {
  private final XStream serializer = new XStream();
  private final IElementInfoProvider<E> elementsInfo;
  private final List<ICanRichValidate<E, ? super ICollection<E>>> validators;
  public CollectionAdapter(IElementInfoProvider<E> elementsInfo) {
    this(elementsInfo, new ArrayList<>());
  }
  
  public CollectionAdapter(IElementInfoProvider<E> elementsInfo, List<ICanRichValidate<E, ? super ICollection<E>>> validators) {
    this.elementsInfo = elementsInfo;
    this.validators = validators;
  }

  {
    serializer.allowTypesByWildcard(new String[]{"net.whitehorizont.apps.organization_collection_manager.core.**"});
    serializer.processAnnotations(StorageXml.class);
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

    // hard code UTF-8 cause who the fuck except shitty windows uses other encoding for files?
    // no array element should every be null
    @SuppressWarnings("null")
    final byte @NonNull[] serializedContent = serializer.toXML(storageXml).getBytes(DEFAULT_ENCODING);
    return serializedContent;
  }

  public RamCollection<E> parse(ByteBuffer fileContent) throws ValidationError, KeyGenerationError, DuplicateElements {
    // receive buffer
    // cast to string
    final String xml_content = DEFAULT_ENCODING.decode(fileContent).toString();
    // parse xml:
    @SuppressWarnings("unchecked")
    StorageXml<E, CollectionMetadata> storageXmlRepresentation = (StorageXml<E, CollectionMetadata>) serializer.fromXML(xml_content);

    final CollectionMetadata collectionMetadata = storageXmlRepresentation.collection.metadata;
    
    final var collection = new RamCollection<E>(this.elementsInfo, collectionMetadata);

    // TODO: move configuration to collection factory
    for (final var validator : this.validators) {
      collection.addValidator(validator);
    }

    
    for (var elementXmlRepresentation : storageXmlRepresentation.collection.elements) {
      collection.insert(elementXmlRepresentation);
    }

    return collection;
    //! reliable integrity check is impossible to implement without custom XML parser 
  }

  @Override
  public RamCollection<E> deserialize(byte[] fileContent) throws ValidationError, ResourceEmpty, KeyGenerationError, DuplicateElements {
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
    return new RamCollection<>(elementsInfo, metadata);
  }

  @Override
  public RamCollection<E> deserializeSafe() {
    final var emptyCollectionMetadata = new CollectionMetadata();
    return new RamCollection<>(elementsInfo, emptyCollectionMetadata);
  }
}
