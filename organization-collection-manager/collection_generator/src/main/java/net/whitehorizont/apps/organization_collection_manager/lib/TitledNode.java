package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class TitledNode<T> extends Node<TitledNode<T>, T> implements IDisplayable {
  private final String displayedName;
  
  public TitledNode(String displayedName, T[] leafs, TitledNode<T>[] children) {
    super(leafs, children);
    this.displayedName = displayedName;

  }

  @Override
  public String getDisplayedName() {
    return displayedName;
  }
}
