package net.whitehorizont.apps.organization_collection_manager.core.storage;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class KeyedCollectionConverter implements Converter {

  @Override
  public boolean canConvert(Class type) {
    return IStorableKeyedCollection.class.isAssignableFrom(type);
  }

  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    final IStorableKeyedCollection<?, ?> collection = (IStorableKeyedCollection<?, ?>) source;
    writer.startNode("collection");
    writer.startNode("collection-metadata");
    collection.getMetadataSnapshot();
    collection.getEveryWithKey$().subscribe((entry) -> {
      context.convertAnother(collection);
    });
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'unmarshal'");
  }
  
}
