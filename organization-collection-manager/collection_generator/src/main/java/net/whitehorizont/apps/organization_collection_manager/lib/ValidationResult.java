package net.whitehorizont.apps.organization_collection_manager.lib;

public class ValidationResult<R> implements IDisplayableMessage {
  public static final String DEFAULT_MESSAGE = "Correct";
  
  private final R result;
  private final String message;
  public ValidationResult(R result, String message) {
    this.result = result;
    this.message = message != null ? message : ValidationResult.DEFAULT_MESSAGE;
  }

  public R getResult() {
    return result;
  }

  @Override
  public String getDisplayedMessage() {
    return message;
  }
  

}
