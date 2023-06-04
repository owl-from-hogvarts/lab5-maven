package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class Node<This extends Node<This, T>, T> {
  private final List<T> leafs;
  private final List<This> children;

  public Node(List<T> leafs, List<This> children) {
    this.leafs = leafs;
    this.children = children;

  }
  
  public List<T> getLeafs() {
    return leafs;
  }
  public List<This> getChildren() {
    return children;
  }
}
