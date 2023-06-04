package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;

@NonNullByDefault
public interface ICollectionElement<P extends IElementPrototype<?>> extends IWithId<UUID_ElementId>, IWithPrototype<P> {}
