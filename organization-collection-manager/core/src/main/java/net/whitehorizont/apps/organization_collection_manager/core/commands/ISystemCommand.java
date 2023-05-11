package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;

@NonNullByDefault
public interface ISystemCommand<@NonNull T> extends IExecutable<T> {
  void setCommandQueue(CommandQueue queue);
  void setCollectionManager(ICollectionManager<?, ?> collectionManager);
}
