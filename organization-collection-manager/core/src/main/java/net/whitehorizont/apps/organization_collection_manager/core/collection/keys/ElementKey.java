package net.whitehorizont.apps.organization_collection_manager.core.collection.keys;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.IFromStringBuilder;
import net.whitehorizont.apps.organization_collection_manager.lib.NumberFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class ElementKey extends BaseId implements Comparable<ElementKey> {
  private static int current = 0;
  private static IFromStringBuilder<Integer> intParser = new NumberFactory();
  
  private final int key;

  public static ElementKey buildFromString(String string) throws ValidationError {
    final var keyInt = intParser.buildFromString(string);
    return new ElementKey(keyInt);
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + key;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ElementKey other = (ElementKey) obj;
    if (key != other.key)
      return false;
    return true;
  }
  
}
