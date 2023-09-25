package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.cli.Streams;
import net.whitehorizont.apps.collection_manager.cli.errors.UnexpectedEndOfFile;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.InsertOrganisationCommand;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElement;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

// element must adhere to ICollectionElement (server's (InputCommand's) requirement)
// otherwise cli command would not be able to issue command to server
// since cli command does not know, what transformations server does on data 
// it may only pass ready made elements to server

// hardcoding types reveals to cli command which transformations server does
// therefore it can pass raw data
@NonNullByDefault
public class Insert
    extends InputElementCommand<OrganisationElement, OrganisationElementWritable>
    implements ICliCommand<IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {

  public Insert(MetadataComposite<?, OrganisationElement, OrganisationElementWritable> metadata, IWritableHostFactory<OrganisationElementWritable> elementFactory,
      Retries retries) {
    super(metadata, elementFactory, retries);
  }

  private static final String DESCRIPTION = "insert element into collection";

  @Override
  public Observable<Void> run(
      CliDependencyManager<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> dependencyManager,
      Stack<String> arguments)
      throws ValidationError, UnexpectedEndOfFile {
    final var key = arguments.pop();

      final var lineReader = dependencyManager.getGenericLineReader();
      final Streams streams = prepareStreams(dependencyManager);

      final var host = getWritableCollectionElement();
      promptForFields(host, lineReader, streams);

      final var insertCommand = new InsertOrganisationCommand(key, host);

      return dependencyManager.getCommandQueue().push(insertCommand);
  }

  @Override
  public Optional<String> getArgumentName() {
    return Optional.of("key");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  // receive streams
  // get collection we operating on
  // on that collection get element prototype
  // for each field of element prototype:
  // if field contains children
  // ...
  // if not, retrieve field's metadata.
  // with that metadata (field name especially) ask for input
  // how prompt knows witch type to return?
  // We create driver for that prompt, witch maps known types to prompt calls
}
