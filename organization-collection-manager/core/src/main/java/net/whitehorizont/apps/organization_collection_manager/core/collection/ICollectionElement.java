package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ICollectionElement<E extends ICollectionElement<E>> extends Comparable<E> {}
