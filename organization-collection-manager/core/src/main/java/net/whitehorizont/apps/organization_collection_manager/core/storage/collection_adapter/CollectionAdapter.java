package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.Collection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IDataSinkSourceFactory;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IFileAdapter;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

public class CollectionAdapter<P, E extends ICollectionElement<P, ? extends BaseId>, F extends IDataSinkSourceFactory<P, E, ? super IBaseCollection<P, E, ?>>>
    implements IFileAdapter<Collection<P, E>> {
  private final XStream serializer = new XStream();
  private final F dataSinkSourceFactory;

  public CollectionAdapter(F dataSinkSourceFactory) {
    this.dataSinkSourceFactory = dataSinkSourceFactory;
  }

  private CollectionXml<P, CollectionMetadata> prepareCollection(IBaseCollection<P, E, CollectionMetadata> collection) {
    final List<ElementXml<P>> elements = collection.getEvery$()
        .map(element -> new ElementXml<>(element.getId().serialize(), element.getPrototype()))
        .toList().blockingGet();
    return new CollectionXml<>(collection.getMetadataSnapshot(), elements);
  }

  @Override
  public byte[] serialize(Collection<P, E> toSerialize) {
    final var collectionXml = prepareCollection(toSerialize);

    final String collectionXmlSerialized = serializer.toXML(collectionXml);
    // algorithm hardcoded for now
    final var storageIntegrity = new FileIntegrity(new Sha256(), collectionXmlSerialized.getBytes(StandardCharsets.UTF_8));
    final var storageMetadata = new FileMetadataXml(storageIntegrity);

    final var storageXml = new StorageXml<>(collectionXml, storageMetadata);

    // hard code UTF-8 cause who the fuck except shitty windows uses other encoding for files?
    return serializer.toXML(storageXml).getBytes(StandardCharsets.UTF_8);
  }

  public Collection<P, E> parse(ByteBuffer fileContent) throws ValidationError {
    // receive buffer
    // cast to string
    final String xml_content = StandardCharsets.UTF_8.decode(fileContent).toString();
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
  public Collection<P, E> deserialize(byte[] fileContent) throws ValidationError {
    return parse(ByteBuffer.wrap(fileContent));
  }
}
