package net.whitehorizont.apps.collection_manager.core.commands.errors;

public class NoSuchUser extends AuthException {
  public NoSuchUser(String login) {
    super("Could not find user with login: " + login);
  }
}
