package net.whitehorizont.apps.collection_manager.core.dependencies;

import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IAuthReceiver;

public interface IProvideAuthReceiver {
  IAuthReceiver getAuthReceiver();
}
