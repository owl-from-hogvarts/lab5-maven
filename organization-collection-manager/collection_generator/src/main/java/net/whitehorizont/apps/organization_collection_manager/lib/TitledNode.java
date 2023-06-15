package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
// raw type used, because otherwise type checking is too strict and inflexible
// E.g. it forbids to have children with unknown parts of generics
// which are necessary for metadata tree
// The restriction exists to ensure that children are actually nodes
// and not just another leafs
public class TitledNode<This extends TitledNode, T> extends Node<This, T> implements IDisplayable {
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
