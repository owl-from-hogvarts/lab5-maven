package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.FilterStartsWithCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;

@NonNullByDefault
public class FilterStartsWith extends BaseElementCommand implements ICliCommand<OrganisationCollectionCommandReceiver> {
  private static final String DESCRIPTION = "display organisations, which name starts with {prefix}";

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends OrganisationCollectionCommandReceiver> dependencyManager,
      Stack<String> arguments) throws Exception {
        final String prefix = arguments.pop().trim().strip();
        final var out = dependencyManager.getStreams().out;
        final var filterCommand = new FilterStartsWithCommand(dependencyManager.getCollectionReceiver(), prefix);
        dependencyManager.getCommandQueue().push(filterCommand).blockingSubscribe(keyElement -> {
          final var element = keyElement.getValue();
          printFields(element.getTree(), keyElement.getKey(), out);
        });

        return Observable.empty();
      }
  
}
