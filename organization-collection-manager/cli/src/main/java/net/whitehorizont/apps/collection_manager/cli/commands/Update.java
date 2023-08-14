package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.commands.UpdateCommand;
import net.whitehorizont.apps.collection_manager.core.collection.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.core.collection.OrganisationElementDefinition.OrganisationElement;
import net.whitehorizont.apps.collection_manager.core.collection.OrganisationElementDefinition.OrganisationElementWritable;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class Update
 extends InputElementCommand<OrganisationElement, OrganisationElementWritable> implements ICliCommand<OrganisationCollectionCommandReceiver> {
  public Update(MetadataComposite<?, OrganisationElement, OrganisationElementWritable> metadata, IWritableHostFactory<OrganisationElementWritable> elementFactory, Retries retries) {
    super(metadata, elementFactory, retries);
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
    final String idString = arguments.pop().trim().strip();
    final var id = OrganisationElementDefinition.ID_METADATA.getValueBuilder().get().buildFromString(idString);

    final var lineReader = dependencyManager.getGenericLineReader();
    final var host = getWritableCollectionElement();
    
    final var streams = prepareStreams(dependencyManager);
    promptForFields(host, lineReader, streams);
  
    final var receiver = dependencyManager.getCollectionReceiver();
    final var updateCommand = new UpdateCommand(id, receiver, host);


    return dependencyManager.getCommandQueue().push(updateCommand);

    // get key-element pair by id
    // hold over deletion of element from collection
    // ask for new element via insert command
    // update new's element id with the old one
    // propose new element to collection
  }
}
