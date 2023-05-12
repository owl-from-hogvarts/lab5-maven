package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class SelectBestStorage<C extends IBaseCollection<?, ?, M>, M extends IWithId<? extends BaseId>>
    implements IStorageSelector<C, M> {

  @Override
  public Iterable<Entry<IBaseStorage<C, M>, Set<C>>> select(Map<IBaseStorage<C, M>, Set<C>> storageAssociations) throws StorageInaccessibleError {
    for (final var store : storageAssociations.entrySet()) {
      assert store != null;
      final var iterable = new ArrayList<Entry<IBaseStorage<C, M>, Set<C>>>();
      iterable.add(store);
      return iterable;
    }

    // TODO: think up better error type; something like NoStoragesAvailable
    throw new StorageInaccessibleError();
  }

}
