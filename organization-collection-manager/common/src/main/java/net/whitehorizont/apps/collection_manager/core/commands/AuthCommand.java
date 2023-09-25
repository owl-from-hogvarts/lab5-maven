package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IWithAuthData;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;

@NonNullByDefault
public class AuthCommand<Return, DependencyProvider extends IProvideAuthReceiver> implements ICommand<Return, DependencyProvider> {
  private final String login;
  private final String password;
  private final ICommand<Return, ? super DependencyProvider> command;

  public AuthCommand(String login, String password, ICommand<Return, ? super DependencyProvider> command) {
    this.login = login;
    this.password = password;
    this.command = command;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public final Observable<@NonNull Return> execute(DependencyProvider dependencyProvider) {
    final var authProvider = dependencyProvider.getAuthReceiver();
    return executeAuthorized(dependencyProvider).delaySubscription(authProvider.login(login, password));
  }

  private Observable<@NonNull Return> executeAuthorized(DependencyProvider dependencyProvider) {
    if (command instanceof IWithAuthData) {
      return ((IWithAuthData<Return, ? super DependencyProvider>)command).executeWithAuthData(dependencyProvider, login);
    }
    return command.execute(dependencyProvider);
  }
  
}
