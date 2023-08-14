package net.whitehorizont.apps.collection_manager.core.collection.keys;

import java.util.UUID;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class UUID_BaseId extends BaseId {

  private final UUID ID;

  protected UUID_BaseId() {
    ID = UUID.randomUUID();
  }

  protected UUID_BaseId(String idString) throws ValidationError {
    try {
      ID = UUID.fromString(idString);
    } catch (IllegalArgumentException e) {
      throw new ValidationError(idString + " can not be parse into UUID!");
    }
  }

  @Override
  public String serialize() {
    return ID.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ID == null) ? 0 : ID.hashCode());
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
    UUID_BaseId other = (UUID_BaseId) obj;
    if (ID == null) {
      if (other.ID != null)
        return false;
    } else if (!ID.equals(other.ID))
      return false;
    return true;
  }
  

  @Override
  public String toString() {
    return this.serialize();
  }
}
