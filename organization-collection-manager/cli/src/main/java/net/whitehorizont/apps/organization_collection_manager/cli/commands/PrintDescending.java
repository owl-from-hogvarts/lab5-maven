package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementDefinition.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.commands.GetDescendingCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class PrintDescending extends BaseElementCommand<OrganisationElement> implements ICliCommand<OrganisationCollectionCommandReceiver> {
  public PrintDescending(MetadataComposite<?, OrganisationElement, ?> metadata) {
    super(metadata);
  }

  private static final String DESCRIPTION = "print organisations sorted by annual turnover by descending order";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends OrganisationCollectionCommandReceiver> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var collection = dependencyManager.getCollectionReceiver();
        final var command = new GetDescendingCommand(collection);
        final var out = dependencyManager.getStreams().out;
        dependencyManager.getCommandQueue().push(command).blockingSubscribe(keyElement -> {
          final var element = keyElement.getValue();
          printFields(element, keyElement.getKey(), out);
        });

        return Observable.empty();
      }
  
}
