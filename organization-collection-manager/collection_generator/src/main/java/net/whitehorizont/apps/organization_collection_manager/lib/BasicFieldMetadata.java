package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class BasicFieldMetadata implements IDisplayable {
  private final String displayedName;

  public BasicFieldMetadata(String displayedName) {
    this.displayedName = displayedName;
  }

  @Override
  public String getDisplayedName() {
    return displayedName;
  }
  
}
