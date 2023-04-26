package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.UUID;

public class UUID_BaseId extends BaseId {

  private final UUID ID = UUID.randomUUID();

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
  
}
