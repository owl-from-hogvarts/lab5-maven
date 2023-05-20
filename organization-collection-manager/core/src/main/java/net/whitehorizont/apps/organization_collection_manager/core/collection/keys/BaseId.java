package net.whitehorizont.apps.organization_collection_manager.core.collection.keys;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public abstract class BaseId implements ISerializableKey {
  public abstract boolean equals(BaseId o);
  public abstract int hashCode();
}
