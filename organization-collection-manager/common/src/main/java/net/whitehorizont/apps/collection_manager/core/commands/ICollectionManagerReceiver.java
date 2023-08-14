package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionManager;

@NonNullByDefault
public interface ICollectionManagerReceiver<C extends ICollection<?>> extends ICollectionManager<C> {

}