package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DuplicateElementsError extends Exception {
  private final IWithId<? extends ISerializableKey> elementStored;
  
  public DuplicateElementsError(IWithId<? extends ISerializableKey> elementStored) {
        this.elementStored = elementStored;
  }

  @Override
  public String getMessage() {
    // TODO: pretty print stored element
    return "Element with id: \"" + elementStored.getId().serialize() + "\" is absolutely identical to element supplied";
  }
}
