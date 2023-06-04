package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.Nullable;

public class ReadonlyField<V, M extends BasicFieldMetadata> {
  protected @Nullable V value;
  public @Nullable V getValue() {
    return value;
  }

  private M metadata;

  public M getMetadata() {
    return metadata;
  }

  /** 
   * ! USE WITH CAUTION !
   * it is now up to you to init field
   */
  protected ReadonlyField(M metadata) {
    this(metadata, null);
  }

  public ReadonlyField(M metadata, @Nullable V initialValue) {
    this.value = initialValue;
    this.metadata = metadata;
  }
}
