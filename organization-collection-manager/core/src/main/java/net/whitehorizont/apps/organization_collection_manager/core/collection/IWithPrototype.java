package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;

public interface IWithPrototype<P extends IElementPrototype<?>> extends IFieldDefinitionNode {
  P getPrototype();
}
