package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface IElementFactory<P extends IElementPrototype<?>, E, V, K extends BaseId> {
  E buildElementFrom(P prototype, V validationObject) throws ValidationError;
  P getElementPrototype();
  K getElementId(String idString) throws ValidationError;
  String getCollectionType();
}
