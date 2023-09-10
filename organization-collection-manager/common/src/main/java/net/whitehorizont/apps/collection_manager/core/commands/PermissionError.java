package net.whitehorizont.apps.collection_manager.core.commands;

public class PermissionError extends Exception {
  public PermissionError(String reason) {
    super("Permission violation! Reason: " + reason);
  }
}
