package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.commands.RemoveCommand;
import net.whitehorizont.apps.collection_manager.core.collection.OrganisationElementDefinition;

@NonNullByDefault
public class RemoveById implements ICliCommand<OrganisationCollectionCommandReceiver> {
  private static final String DESCRIPTION = "removes element with specified id";

  @Override
  public Optional<String> getArgument() {
    return Optional.of("id");
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
        // DONE: get rid of UUID cast
        final var id = OrganisationElementDefinition.ID_METADATA.getValueBuilder().get().buildFromString(idString);

        final var removeCommand = new RemoveCommand(collection, id);
        dependencyManager.getCommandQueue().push(removeCommand).blockingSubscribe();
        return Observable.empty();
      }
  
}
