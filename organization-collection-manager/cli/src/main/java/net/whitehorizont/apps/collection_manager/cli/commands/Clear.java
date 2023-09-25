package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.ClearOrganisationCommand;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;

@NonNullByDefault
public class Clear implements ICliCommand<IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private static final String DESCRIPTION = "delete all elements from collection";

  @Override
  public Optional<String> getArgumentName() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> dependencyManager, Stack<String> arguments) throws Exception {
    final var clear = new ClearOrganisationCommand();
    return dependencyManager.getCommandQueue().push(clear);
  }
  
}
