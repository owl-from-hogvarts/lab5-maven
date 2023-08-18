package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.GetDescendingCommand;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class PrintDescending extends BaseElementCommand<OrganisationElementFull> implements ICliCommand<IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  public PrintDescending(MetadataComposite<?, OrganisationElementFull, ?> metadata) {
    super(metadata);
  }

  private static final String DESCRIPTION = "print organisations sorted by annual turnover by descending order";

  @Override
  public Optional<String> getArgumentName() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var command = new GetDescendingCommand();
        final var out = dependencyManager.getStreams().out;
        dependencyManager.getCommandQueue().push(command).blockingSubscribe(keyElement -> {
          final var element = keyElement.getValue();
          printFields(element, keyElement.getKey(), out);
        });

        return Observable.empty();
      }
  
}
