package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;

@NonNullByDefault
public class ElementKey implements ISerializableKey, Comparable<ElementKey> {
  private static int current = 0;
  
  private final int key;

  public static ElementKey next() {
    final var newKey = new ElementKey(current);
    current += 1;

    return newKey;
  }

  private ElementKey(int key) {
    this.key = key;
  }

  @Override
  public String serialize() {
    return Integer.toString(key);
  }

  @Override
  public int compareTo(ElementKey other) {
    return other.key - this.key;
  }
  
}
