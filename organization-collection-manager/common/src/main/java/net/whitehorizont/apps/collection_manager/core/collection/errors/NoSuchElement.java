package net.whitehorizont.apps.collection_manager.core.collection.errors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.core.collection.keys.ISerializableKey;

@NonNullByDefault
public class NoSuchElement extends Exception {

  public NoSuchElement(ISerializableKey key) {
    this(key, null);
  }

  public NoSuchElement(ISerializableKey key, String owner) {
    this(key, owner, null);
  }

  public NoSuchElement(ISerializableKey key, String owner, @Nullable Throwable cause) {
    super(buildErrorMessage(key, owner), cause);
  }

  private static String buildErrorMessage(ISerializableKey key, @Nullable String owner) {
    String ownerMessage = "";
    if (owner != null) {
      ownerMessage = " and belongs to user '" + owner + "'";
    }
    return "Collection does not contain element, which can be addressed by " + key.serialize() + ownerMessage;
  }
  
}
