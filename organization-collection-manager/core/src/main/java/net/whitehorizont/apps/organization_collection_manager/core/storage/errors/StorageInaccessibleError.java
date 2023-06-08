package net.whitehorizont.apps.organization_collection_manager.core.storage.errors;

public class StorageInaccessibleError extends Exception {

  @Override
  public String getMessage() {
    return "Storage inaccessible!";
  }

}
