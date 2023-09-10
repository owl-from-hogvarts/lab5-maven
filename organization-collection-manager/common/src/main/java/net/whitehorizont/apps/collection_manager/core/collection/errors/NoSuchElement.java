package net.whitehorizont.apps.collection_manager.core.collection.errors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.core.collection.keys.ISerializableKey;

@NonNullByDefault
public class NoSuchElement extends Exception {

  public NoSuchElement(ISerializableKey key) {
    this(key, null);
  }

  public NoSuchElement(ISerializableKey key, @Nullable Throwable cause) {
    super(buildErrorMessage(key), cause);
  }

  private static String buildErrorMessage(ISerializableKey key) {
    return "Collection does not contain element, which can be addressed by " + key.serialize();
  }
  
}
