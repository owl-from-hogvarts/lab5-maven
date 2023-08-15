package net.whitehorizont.apps.collection_manager.core.dependencies;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;

/**
 * Alias for all interfaces. Server should implement it. Client should use it. 
 * This allows slightly better type safety, because client does not know
 * server capabilities in advance. That is client may 
 * require interfaces which are not yet implemented on server side,
 * without getting any compile time errors.
 * 
 * In essence, this is impossible to check at compile time.
 * Runtime check will complicate things a lot.
 */
@NonNullByDefault
public interface IUniversalCoreProvider<CR extends ICollectionCommandReceiver<E>, E extends ICollectionElement<E>> extends IProvideCollectionReceiver<CR>,IProvideNothing, IProvideCollectionManagerReceiver<ICollection<E>> {
  
}
