package net.whitehorizont.apps.organization_collection_manager.cli;

import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;

public class CliDependencyManager<CM extends ICollectionManager<?, ?>> {
  private final CM collectionManager;

  public CM getCollectionManager() {
    return collectionManager;
  }

  public CliDependencyManager(CM collectionManager) {
    this.collectionManager = collectionManager;
  }
}
