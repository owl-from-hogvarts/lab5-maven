package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("collection")
public class CollectionXml<E, M> {
  final M metadata;
  final List<ElementXml<E>> elements;
  CollectionXml(M metadata, List<ElementXml<E>> elements) {
    this.metadata = metadata;
    this.elements = elements;
  }
}
