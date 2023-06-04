package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class Node<This extends Node<This, T>, T> {
  private final List<T> leafs;
  private final List<This> children;

  public Node(T[] leafs, This[] children) {
    this.leafs = Arrays.asList(leafs);
    this.children = Arrays.asList(children);

  }
  
  public List<T> getLeafs() {
    return leafs;
  }
  public List<This> getChildren() {
    return children;
  }
}
