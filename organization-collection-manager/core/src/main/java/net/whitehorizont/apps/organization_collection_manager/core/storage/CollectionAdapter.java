package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.nio.ByteBuffer;

import com.thoughtworks.xstream.XStream;

public class CollectionAdapter<E, M> implements IFileAdapter<E, M> {
  private final XStream serializer = new XStream();
  
  public CollectionAdapter() {
    serializer.registerConverter(new KeyedCollectionConverter());
  }
  
  public ByteBuffer serialize(IStorableKeyedCollection<E, M> toSerialize) {

    final String metadata = serializer.toXML(toSerialize.getMetadataSnapshot());
    toSerialize.getEveryWithKey$().subscribe((entry) -> {
    });
    
    // serialize collection metadata
    // serialize collection elements
  }
  public void parse() {
    // receive buffer
    // cast to string
    // parse xml:
      // file metadata
      // check integrity
      // collection metadata
      // array of key element pairs
  }

  @Override
  public IStorableKeyedCollection<E, M> deserialize(ByteBuffer fileContent) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
  }

}
