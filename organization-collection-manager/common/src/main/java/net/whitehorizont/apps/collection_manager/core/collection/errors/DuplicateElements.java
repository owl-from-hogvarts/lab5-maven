package net.whitehorizont.apps.collection_manager.core.collection.errors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.core.collection.keys.ISerializableKey;

@NonNullByDefault
public class DuplicateElements extends Exception {

  public DuplicateElements(ISerializableKey key) {
    this(key, null);
  }

  public DuplicateElements(ISerializableKey key, @Nullable Throwable cause) {
    super(buildErrorMessage(key), cause);
  }

  private static String buildErrorMessage(ISerializableKey key) {
    return "Collection already contains element, which can be addressed by " + key.serialize();
  }
  
}
