package net.whitehorizont.apps.collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
@FunctionalInterface
public interface CollectionMiddleware<E extends ICollectionElement<E>> {
  /**
   * takes a collection and collection's element, on which operation is performed
   * @throws ValidationError
   */
  void accept(ICollection<E> collection, E element) throws ValidationError;
}
