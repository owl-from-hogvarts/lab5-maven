package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;

@NonNullByDefault
public interface ICliCommand<CR extends CollectionCommandReceiver<?, ?>> {
  Optional<String> getArgument();
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
  Observable<Void> run(CliDependencyManager<? extends CR> dependencyManager, Stack<String> arguments) throws Exception;
}
