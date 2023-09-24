package net.whitehorizont.apps.collection_manager.cli.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public abstract class AuthCommands {
  protected static LoginData askForCredentials(CliDependencyManager<? extends IProvideAuthReceiver> dependencyManager) throws ValidationError {
    final var reader = dependencyManager.getGenericLineReader();
    final String login = reader.readLine("Login: ");
    final String password = reader.readLine("Password: ", '*');

    if (login.length() < 3) {
      throw new ValidationError("Login should be longer than 2 symbols");
    }

    if (password.length() < 3) {
      throw new ValidationError("Password should be longer than 2 symbols");
    }

    return new LoginData(login, password);
  }

  protected static record LoginData(String login, String password) {
  }
}
