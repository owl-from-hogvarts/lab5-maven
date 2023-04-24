package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import net.whitehorizont.apps.organization_collection_manager.core.collection.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IFileAdapter;

public class CollectionAdapter<C extends IBaseCollection<?, E, M>, M extends IWithId<CollectionId>, E extends IWithId<? extends BaseId>>
    implements IFileAdapter<C> {
  private final XStream serializer = new XStream();

  public CollectionAdapter() {
  }

  private CollectionXml<E, M> prepareCollection(C collection) {
    final List<ElementXml<E>> elements = collection.getEvery$()
        .map(element -> new ElementXml<>(element.getId().serialize(), element))
        .toList().blockingGet();
    return new CollectionXml<>(collection.getMetadataSnapshot(), elements);
  }

  @Override
  public byte[] serialize(C toSerialize) {
    final var collectionXml = prepareCollection(toSerialize);

    final String collectionXmlSerialized = serializer.toXML(collectionXml);
    // algorithm hardcoded for now
    final var storageIntegrity = new FileIntegrity(new Sha256(), collectionXmlSerialized.getBytes(StandardCharsets.UTF_8));
    final var storageMetadata = new FileMetadataXml(storageIntegrity);

    final var storageXml = new StorageXml<>(collectionXml, storageMetadata);

    // hard code UTF-8 cause who the fuck except shitty windows uses other encoding for files?
    return serializer.toXML(storageXml).getBytes(StandardCharsets.UTF_8);
  }

  public void parse(ByteBuffer fileContent) {
    // receive buffer
    // cast to string
    final String xml_content = StandardCharsets.UTF_8.decode(fileContent).toString();
    // parse xml:
    @SuppressWarnings("unchecked")
    StorageXml<?, M> storageXmlRepresentation = (StorageXml<?, M>) serializer.fromXML(xml_content);

    final var integrityAlgorithm = new Sha256();
    if (storageXmlRepresentation.metadata.integrity.algorithmName == integrityAlgorithm.getDisplayedName()) {

    }
    // storageXmlRepresentation.metadata.integrity.integrityData
      // file metadata
      // check integrity
      // collection metadata
      // array of key element pairs
  }

  @Override
  public C deserialize(byte[] fileContent) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
  }
}
