package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.thoughtworks.xstream.XStream;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.Collection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IDataSinkSourceFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IFileAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

@NonNullByDefault
public class CollectionAdapter<P, E extends ICollectionElement<P, ? extends BaseId>, F extends IDataSinkSourceFactory<P, E, ? super IBaseCollection<P, E, ?>>>
    implements IFileAdapter<Collection<P, E>, CollectionMetadata> {
  private final XStream serializer = new XStream();
  private final F dataSinkSourceFactory;
  @SuppressWarnings("null")
  private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

  public CollectionAdapter(F dataSinkSourceFactory) {
    this.dataSinkSourceFactory = dataSinkSourceFactory;
  }

  private CollectionXml<P, CollectionMetadata> prepareCollection(IBaseCollection<P, E, CollectionMetadata> collection) {
    final var elements = collection.getEvery$()
        .map(element -> new ElementXml<>(element.getId().serialize(), element.getPrototype()))
        .toList().blockingGet();
    return new CollectionXml<>(collection.getMetadataSnapshot(), elements);
  }

  @Override
  public byte[] serialize(Collection<P, E> toSerialize) {
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

  public Collection<P, E> parse(ByteBuffer fileContent) throws ValidationError {
    // receive buffer
    // cast to string
    final String xml_content = DEFAULT_ENCODING.decode(fileContent).toString();
    // parse xml:
    @SuppressWarnings("unchecked")
    StorageXml<P, CollectionMetadata> storageXmlRepresentation = (StorageXml<P, CollectionMetadata>) serializer.fromXML(xml_content);

    final CollectionMetadata collectionMetadata = storageXmlRepresentation.collection.metadata;
    
    final Collection<P, E> collection = new Collection<P, E>(this.dataSinkSourceFactory, collectionMetadata);

    final var collectionDataSink = collection.getDataSink();
    for (var elementXmlRepresentation : storageXmlRepresentation.collection.elements) {
      collectionDataSink.supply(elementXmlRepresentation.body);
    }

    return collection;
    //! reliable integrity check is impossible to implement without custom XML parser 
  }

  @Override
  public Collection<P, E> deserialize(byte[] fileContent) throws ValidationError, ResourceEmpty {
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
  public Collection<P, @NonNull E> deserializeSafe(CollectionMetadata metadata) {
    return new Collection<>(dataSinkSourceFactory, metadata);
  }

  @Override
  public Collection<P, E> deserializeSafe() {
    final var emptyCollectionMetadata = new CollectionMetadata(new Builder());
    return new Collection<>(dataSinkSourceFactory, emptyCollectionMetadata);
  }
}
