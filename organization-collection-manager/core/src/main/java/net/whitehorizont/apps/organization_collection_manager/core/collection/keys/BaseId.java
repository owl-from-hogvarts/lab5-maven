package net.whitehorizont.apps.organization_collection_manager.core.collection.keys;

public abstract class BaseId implements ISerializableKey {
  public abstract boolean equals(Object o);
  public abstract int hashCode();
}
