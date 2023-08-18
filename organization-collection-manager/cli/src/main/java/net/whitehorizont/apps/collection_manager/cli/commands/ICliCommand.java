package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;

@NonNullByDefault
public interface ICliCommand<DP> {
  Optional<String> getArgumentName();
  String getCommandDescription();

  /**
   * provide full access to input and output for command to 
   * freely communicate with user 
   * 
   * @param in
   * @param out
   * @param err
   * @return
   * @throws Exception if command can't handle something, let's just fail and pass error to global error handler
   */
  Observable<Void> run(CliDependencyManager<? extends DP> dependencyManager, Stack<String> arguments) throws Exception;
}
