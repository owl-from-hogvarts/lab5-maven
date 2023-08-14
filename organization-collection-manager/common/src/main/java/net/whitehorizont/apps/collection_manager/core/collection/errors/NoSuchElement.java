package net.whitehorizont.apps.collection_manager.core.collection.errors;

import net.whitehorizont.apps.collection_manager.core.collection.keys.ISerializableKey;

public class NoSuchElement extends Exception {

  private final String key;

  public NoSuchElement(ISerializableKey key) {
    this.key = key.serialize();
  }

  @Override
  public String getMessage() {
    return "Collection does not contain element, which can be addressed by " + key;
  }
  
}
