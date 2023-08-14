package net.whitehorizont.apps.collection_manager.core.collection.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ICollectionElement<E extends ICollectionElement<E>> extends Comparable<E> {}
