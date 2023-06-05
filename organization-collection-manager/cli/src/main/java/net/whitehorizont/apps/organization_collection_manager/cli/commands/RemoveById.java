package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.RemoveCommand;

@NonNullByDefault
public class RemoveById implements ICliCommand<OrganisationCollectionCommandReceiver> {
  private static final String DESCRIPTION = "removes element with specified id";

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
        final var collection = dependencyManager.getCollectionReceiver();
        
        final String idString = arguments.pop().trim().strip();
        // TODO: get rid of this cast
        final var id = (UUID_ElementId) collection.getElementIdFromString(idString);

        final var removeCommand = new RemoveCommand(collection, id);
        dependencyManager.getCommandQueue().push(removeCommand).blockingSubscribe();
        return Observable.empty();
      }
  
}
