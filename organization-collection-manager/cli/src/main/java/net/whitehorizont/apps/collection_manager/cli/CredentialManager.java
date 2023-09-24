package net.whitehorizont.apps.collection_manager.cli;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.AuthCommand;
import net.whitehorizont.apps.collection_manager.core.commands.errors.AuthException;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;

@NonNullByDefault
public class CredentialManager<DP extends IProvideAuthReceiver> implements ICommandQueue<DP> {
  private final ICommandQueue<DP> nextCommandQueue;
  private Optional<LoginData> loginData = Optional.empty();

  public void setLoginData(LoginData loginData) {
    this.loginData = Optional.of(loginData);
  }

  public CredentialManager(ICommandQueue<DP> nextCommandQueue) {
    this.nextCommandQueue = nextCommandQueue;
  }

  @Override
  public <T> Observable<@NonNull T> push(ICommand<T, ? super DP> command) {
    // server discards commands which are not of type AuthCommand
    try {
      command = withAuth(command);
      return nextCommandQueue.push(command);
    } catch (AuthException e) {
      return Observable.error(e);
    }
  }

  private <T> ICommand<T, DP> withAuth(ICommand<T, ? super DP> command) throws AuthException {
    if (loginData.isEmpty()) {
      throw new AuthException("You must first log in with 'login' command");
    }
    return new AuthCommand<>(loginData.get().login(), loginData.get().password(), command);
  }

}
