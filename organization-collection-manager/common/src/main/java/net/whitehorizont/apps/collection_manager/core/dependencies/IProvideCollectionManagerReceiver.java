package net.whitehorizont.apps.collection_manager.core.dependencies;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.commands.ICollectionManagerReceiver;

@NonNullByDefault
public interface IProvideCollectionManagerReceiver<C extends ICollection<?>> {
  ICollectionManagerReceiver<C> getCollectionManagerReceiver();
}
