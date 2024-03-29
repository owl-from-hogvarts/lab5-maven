package net.whitehorizont.apps.collection_manager.core.storage.collection_adapter;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("storage")
public class StorageXml<E, M> {
  final CollectionXml<E, M> collection;

  public CollectionXml<E, M> getCollection() {
    return collection;
  }

  @XStreamAlias("meta")
  final FileMetadataXml metadata;

  StorageXml(CollectionXml<E, M> collections, FileMetadataXml metadata) {
    this.collection = collections;
    this.metadata = metadata;
  }

}
