package net.whitehorizont.apps.collection_manager.core.commands.errors;

public class AuthException extends Exception {
  public AuthException(String message) {
    super(message);
  }

  public AuthException(String message, Throwable cause) {
    super(message, cause);
  }
}
