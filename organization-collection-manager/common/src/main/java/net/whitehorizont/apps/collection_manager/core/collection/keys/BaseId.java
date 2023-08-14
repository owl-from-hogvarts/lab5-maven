package net.whitehorizont.apps.collection_manager.core.collection.keys;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public abstract class BaseId implements ISerializableKey {
  public abstract boolean equals(Object o);
  public abstract int hashCode();
}
