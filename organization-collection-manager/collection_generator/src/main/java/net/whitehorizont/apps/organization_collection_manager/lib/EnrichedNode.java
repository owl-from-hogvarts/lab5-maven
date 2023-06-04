package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.annotations.NonNull;

@NonNullByDefault
public class EnrichedNode<E, T> extends TitledNode<Pair<E, T>> {

  public EnrichedNode(String displayedName, @NonNull Pair<E, T>[] leafs, TitledNode<Pair<E, T>>[] children) {
    super(displayedName, leafs, children);
  }}
