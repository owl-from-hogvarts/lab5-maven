package net.whitehorizont.apps.collection_manager.core.dependencies;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;

@NonNullByDefault
public interface IProvideCollectionReceiver<Receiver extends ICollectionCommandReceiver<?>> {
  Receiver getCollectionReceiver();
}
