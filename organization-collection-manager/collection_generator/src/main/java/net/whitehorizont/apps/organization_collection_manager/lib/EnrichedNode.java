package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

@NonNullByDefault
public class EnrichedNode<E, T> extends TitledNode<EnrichedNode<E, T>, Pair<E, T>> {

  public EnrichedNode(String displayedName, List<Pair<E, T>> leafs, List<EnrichedNode<E, T>> children) {
    super(displayedName, leafs, children);
  }}
