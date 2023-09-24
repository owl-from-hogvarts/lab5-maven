package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;

@NonNullByDefault
public class LoginCommand implements ICommand<Void, IProvideAuthReceiver> {
  private final String login;
  private final String password;

  public LoginCommand(String login, String password) {
    this.login = login;
    this.password = password;
  }

  @Override
  public Observable<Void> execute(IProvideAuthReceiver dependencyProvider) {
      return dependencyProvider.getAuthReceiver().login(login, password);
  }
  
}
