package net.whitehorizont.apps.collection_manager.core.storage.collection_adapter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.whitehorizont.apps.collection_manager.core.crypto.ICryptoProvider;

public class FileIntegrity {
  @XStreamAlias("algorithm")
  final String algorithmName;
  final byte[] integrityData;

  FileIntegrity(ICryptoProvider algorithm, byte[] data) {
    algorithmName = algorithm.getDisplayedName();
    integrityData = algorithm.apply(data);
  }
}
