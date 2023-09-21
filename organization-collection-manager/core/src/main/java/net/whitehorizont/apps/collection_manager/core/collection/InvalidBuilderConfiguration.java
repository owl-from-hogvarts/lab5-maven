package net.whitehorizont.apps.collection_manager.core.collection;

public class InvalidBuilderConfiguration extends Error {
  public InvalidBuilderConfiguration(String fieldName) {
    super(fieldName + " is not initialized");
  }
}
