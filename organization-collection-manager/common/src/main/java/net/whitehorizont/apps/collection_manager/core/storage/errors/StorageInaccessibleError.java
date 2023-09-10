package net.whitehorizont.apps.collection_manager.core.storage.errors;

public class StorageInaccessibleError extends Exception {

  public StorageInaccessibleError(Throwable cause) {
    super("Storage inaccessible!", cause);
  }

}
