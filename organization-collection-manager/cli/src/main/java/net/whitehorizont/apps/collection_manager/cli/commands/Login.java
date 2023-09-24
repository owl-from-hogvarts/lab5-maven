package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.commands.LoginCommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;

@NonNullByDefault
public class Login extends AuthCommands implements ICliCommand<IProvideAuthReceiver> {
  private static final String DESCRIPTION = "logins you to our system";
  

  @Override
  public Optional<String> getArgumentName() {
    return  Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<?> run(CliDependencyManager<? extends IProvideAuthReceiver> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var loginData = askForCredentials(dependencyManager);
        return dependencyManager.getCommandQueue().push(new LoginCommand(loginData.login(), loginData.password()));
      }
  
}
