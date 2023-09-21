package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.FilterStartsWithCommand;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class FilterStartsWith extends BaseElementCommand<OrganisationElementFull> implements ICliCommand<IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  public FilterStartsWith(MetadataComposite<?, OrganisationElementFull, ?> metadata) {
    super(metadata);
  }

  private static final String DESCRIPTION = "display organisations, which name starts with {prefix}";

  @Override
  public Optional<String> getArgumentName() {
    return Optional.of("prefix");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<?> run(CliDependencyManager<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> dependencyManager,
      Stack<String> arguments) throws Exception {
        final String prefix = arguments.pop().trim().strip();
        final var out = dependencyManager.getStreams().out;
        final var filterCommand = new FilterStartsWithCommand(prefix);
        return dependencyManager.getCommandQueue().push(filterCommand).doOnNext(keyElement -> {
          final var element = keyElement.getValue1();
          printFields(element, keyElement.getValue0(), out);
        });
      }
  
}
