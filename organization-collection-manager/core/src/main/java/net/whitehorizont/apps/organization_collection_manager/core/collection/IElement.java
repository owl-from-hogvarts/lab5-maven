package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;

public interface IElement<P extends IElementPrototype<?>> extends IFieldDefinitionNode {
  P getPrototype();
}
