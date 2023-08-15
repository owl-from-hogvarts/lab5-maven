package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface ICollectionCommandReceiver<E extends ICollectionElement<E>> extends ICollection<E> {
  CollectionMetadataComputed getMetadataTree();
  void insert(String key, E element) throws ValidationError, DuplicateElements;
}