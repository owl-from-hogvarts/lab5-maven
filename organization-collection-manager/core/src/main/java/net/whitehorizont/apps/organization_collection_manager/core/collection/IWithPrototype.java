package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.IReadonlyTreeProvider;

public interface IWithPrototype<P extends IElementPrototype<?>> extends IReadonlyTreeProvider {
  P getPrototype();
}
