package net.whitehorizont.apps.organization_collection_manager.cli.errors;

public class RecursionDetected extends Exception {
  private static final String ERROR_MESSAGE = "Recursion detected! Script can not call itself or any of it's callers!";
  

  @Override
  public String getMessage() {
    return ERROR_MESSAGE;
  }
  
}
