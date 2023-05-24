package net.whitehorizont.apps.organization_collection_manager.lib.validators;

public class ValidationError extends Exception {

  public ValidationError() {
  }

  public ValidationError(String message) {
    super(message);
  }

  public ValidationError(Throwable cause) {
    super(cause);
  }

  public ValidationError(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
