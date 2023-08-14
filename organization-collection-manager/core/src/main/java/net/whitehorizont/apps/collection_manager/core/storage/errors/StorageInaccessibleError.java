package net.whitehorizont.apps.collection_manager.core.storage.errors;

public class StorageInaccessibleError extends Exception {

  @Override
  public String getMessage() {
    return "Storage inaccessible!";
  }

}
