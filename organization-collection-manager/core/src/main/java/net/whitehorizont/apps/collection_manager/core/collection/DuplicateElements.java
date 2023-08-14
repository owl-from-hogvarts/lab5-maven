package net.whitehorizont.apps.collection_manager.core.collection;

import net.whitehorizont.apps.collection_manager.core.collection.keys.ISerializableKey;

public class DuplicateElements extends Exception {

  private final String key;

  public DuplicateElements(ISerializableKey key) {
    this.key = key.serialize();
  }

  @Override
  public String getMessage() {
    return "Collection already contains element, which can be addressed by " + key;
  }
  
}
