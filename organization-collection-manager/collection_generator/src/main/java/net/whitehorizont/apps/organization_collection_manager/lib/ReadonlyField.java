package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.Nullable;

public class ReadonlyField<V> {
  protected @Nullable V value;
  public @Nullable V getValue() {
    return value;
  }

  private BasicFieldMetadata metadata;

  public BasicFieldMetadata getMetadata() {
    return metadata;
  }

  /** 
   * ! USE WITH CAUTION !
   * it is now up to you to init field
   */
  protected ReadonlyField(BasicFieldMetadata metadata) {
    this(metadata, null);
  }

  public ReadonlyField(BasicFieldMetadata metadata, @Nullable V initialValue) {
    this.value = initialValue;
    this.metadata = metadata;
  }
}
