package net.whitehorizont.apps.collection_manager.cli.errors;

public class UnknownCommand extends Exception {

  private static String computeMessage(String command) {
    return "Unknown command: " + command + ". For list of known commands type \"help\"";
  }

  public UnknownCommand(String message) {
    super(computeMessage(message));
  }  
}
