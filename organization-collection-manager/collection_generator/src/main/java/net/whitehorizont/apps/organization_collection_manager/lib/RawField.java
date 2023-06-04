package net.whitehorizont.apps.organization_collection_manager.lib;

public class RawField<V> {
  private V value;

  public RawField(V initialValue) {
    this.value = initialValue;
  }

  protected void setValue(V value) {
    this.value = value;
  }

  public V getValue() {
    return value;
  }
}
