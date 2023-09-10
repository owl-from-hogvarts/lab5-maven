package net.whitehorizont.apps.collection_manager.cli.errors;

public class RecursionDetected extends Exception {
  private static final String ERROR_MESSAGE = "Recursion detected! Script can not call itself or any of it's callers!";

  public RecursionDetected() {
    super(ERROR_MESSAGE);
  }
  
}
