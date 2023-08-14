package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;

@NonNullByDefault
public interface ICollectionCommandReceiver<E extends ICollectionElement<E>> extends ICollection<E> {
  CollectionMetadataComputed getMetadataTree();
}