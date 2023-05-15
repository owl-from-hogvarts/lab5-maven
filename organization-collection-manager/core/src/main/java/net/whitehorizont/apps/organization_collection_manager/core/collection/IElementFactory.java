package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

public interface IElementFactory<P extends IElementPrototype<?>, E, V> {
  E buildElementFrom(P prototype, V validationObject) throws ValidationError;
  P getElementPrototype();
}
