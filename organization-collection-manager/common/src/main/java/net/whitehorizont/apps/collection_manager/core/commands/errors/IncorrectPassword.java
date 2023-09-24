package net.whitehorizont.apps.collection_manager.core.commands.errors;

public class IncorrectPassword extends AuthException {

  public IncorrectPassword(String login) {
    super("Incorrect password for user with login: " + login);
  }
  
}
