package net.whitehorizont.apps.organization_collection_manager.cli.errors;

public class TerminalUnavailable extends Exception {

  private static final String MESSAGE = "Terminal unavailable";

  @Override
  public String getMessage() {
    return MESSAGE;
  }
  
}
