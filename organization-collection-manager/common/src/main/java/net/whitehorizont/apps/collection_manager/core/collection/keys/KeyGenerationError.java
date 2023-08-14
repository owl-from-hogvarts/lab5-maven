package net.whitehorizont.apps.collection_manager.core.collection.keys;

public class KeyGenerationError extends Exception {

  private static final String DEFAULT_MESSAGE = "Could not generate key for new element! Probably the Universe is broken at this point";
  private final String message;

  public KeyGenerationError() {
    this.message = DEFAULT_MESSAGE;
  }

  public KeyGenerationError(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
  
}
