package net.whitehorizont.apps.collection_manager.core.storage.operations;

import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;

public interface IWithDelete<K extends BaseId> {
    /**
   * Deletes collection from storage by key
   * @param key
   */
  void delete(K key);
}
