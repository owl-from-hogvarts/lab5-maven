package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class FileIntegrity {
  @XStreamAlias("algorithm")
  final String algorithmName;
  final byte[] integrityData;

  FileIntegrity(IntegrityAlgorithm algorithm, byte[] data) {
    algorithmName = algorithm.getDisplayedName();
    integrityData = algorithm.apply(data);
  }
}
