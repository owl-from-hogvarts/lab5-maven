package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.UpdateCommand;

@NonNullByDefault
public class Update
 extends InputElementCommand implements ICliCommand<OrganisationCollectionCommandReceiver> {
  public Update(Retries retries) {
    super(retries);
  }

  private static final String DESCRIPTION = "replace element with id by new one";
  

  @Override
  public Optional<String> getArgument() {
    return Optional.of("id");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends OrganisationCollectionCommandReceiver> dependencyManager, Stack<String> arguments) throws Exception {
    final var collection = dependencyManager.getCollectionReceiver();
    
    final String idString = arguments.pop().trim().strip();
    final var id = collection.getElementIdFromString(idString);

    final var lineReader = dependencyManager.getGenericLineReader();

    final var receiver = dependencyManager.getCollectionReceiver();
    final var updateCommand = new UpdateCommand(id, receiver, (oldPrototype) -> {
      final var streams = prepareStreams(dependencyManager);
      promptForFields(oldPrototype, lineReader, streams);
      return oldPrototype;
    });


    return dependencyManager.getCommandQueue().push(updateCommand);

    // get key-element pair by id
    // hold over deletion of element from collection
    // ask for new element via insert command
    // update new's element id with the old one
    // propose new element to collection
  } 
}
