package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.thoughtworks.xstream.XStream;

import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IFileAdapter;

public class CollectionAdapter<C extends IBaseCollection<?, ?, M>, M extends IWithId<CollectionId>> implements IFileAdapter<C> {
  private final XStream serializer = new XStream();
  
  public CollectionAdapter() {
  }
  
  @Override
  public ByteBuffer serialize(C toSerialize) {
    
    final String xml = serializer.toXML(toSerialize);
    return ByteBuffer.wrap(xml.getBytes(StandardCharsets.UTF_8));
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
  public C deserialize(ByteBuffer fileContent) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
  }
}
