package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
@FunctionalInterface
public interface IStorageSelector<C extends IBaseCollection<?, ?, ?>, M extends IWithId<? extends BaseId>> {
  Iterable<Entry<IBaseStorage<C, M>, Set<C>>> select(Map<IBaseStorage<C, M>, Set<C>> storageAssociations) throws StorageInaccessibleError;
}
