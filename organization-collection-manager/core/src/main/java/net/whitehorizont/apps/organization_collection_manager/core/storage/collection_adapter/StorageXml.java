package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class StorageXml<E, M> {
  final List<CollectionXml<E, M>> collections;

  public List<CollectionXml<E, M>> getCollections() {
    return collections;
  }

  @XStreamAlias("meta")
  final FileMetadataXml metadata;

  StorageXml(List<CollectionXml<E, M>> collections, FileMetadataXml metadata) {
    this.collections = collections;
    this.metadata = metadata;
  }

}
