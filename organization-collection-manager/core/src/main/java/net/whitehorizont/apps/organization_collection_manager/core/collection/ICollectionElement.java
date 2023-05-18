package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;

@NonNullByDefault
public interface ICollectionElement<P extends IElementPrototype<?>> extends IWithId<BaseId> {
  P getPrototype();
  Iterable<FieldDefinition<?, ?>> getFields();
}
