package net.whitehorizont.apps.collection_manager.core.collection.keys;

public class KeyGenerationError extends Exception {

  private static final String DEFAULT_MESSAGE = "Could not generate key for new element! Probably the Universe is broken at this point";

  public KeyGenerationError() {
    this(DEFAULT_MESSAGE);
  }

  public KeyGenerationError(String message) {
    super(message);
  }
  
}
