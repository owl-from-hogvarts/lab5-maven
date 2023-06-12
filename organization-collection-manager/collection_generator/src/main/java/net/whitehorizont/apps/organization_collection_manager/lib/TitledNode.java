package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class TitledNode<This extends TitledNode<This, T>, T> extends Node<This, T> implements IDisplayable {
  private final String displayedName;
  
  public TitledNode(String displayedName, List<T> leafs, List<This> children) {
    super(leafs, children);
    this.displayedName = displayedName;

  }

  @Override
  public String getDisplayedName() {
    return displayedName;
  }
}
