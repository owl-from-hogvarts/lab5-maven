package net.whitehorizont.apps.organization_collection_manager.core.collection.collection_manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IBaseStorage;

@NonNullByDefault
public class StandardSelectStorage<C extends ICollection<?, ?, M>, M extends IWithId<? extends BaseId>> implements IStorageSelector<C, M> {

  @Override
  public Iterable<Entry<IBaseStorage<C, M>, Set<C>>> select(Map<IBaseStorage<C, M>, Set<C>> storageAssociations) {
    final var associations = storageAssociations.entrySet();
    return associations; // похуй
  }
  
}
