package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideNothing;

@NonNullByDefault
public class WhoAmI implements ICliCommand<IProvideNothing> {
  private static final String DESCRIPTION = "shows user you are currently logged in";

  @Override
  public Optional<String> getArgumentName() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<?> run(CliDependencyManager<? extends IProvideNothing> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var loginData = dependencyManager.getCredentialManager().getLoginData();
        final var out = dependencyManager.getStreams().out;

        if (loginData.isEmpty()) {
          out.println("You are currently not logged in. To log in, use 'login' command");
          return Observable.empty();
        }

        out.println("You are logged in as: " + loginData.get().login());
        return Observable.empty();
      }
  
}
