package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.CountCommand;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationType;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.EnumFactory;

@NonNullByDefault
public class CountByType implements ICliCommand<IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private static final String DESCRIPTION = "Count element of collection which have type specified";
  private final EnumFactory<OrganisationType> enumFactory = new EnumFactory<>(OrganisationType.class);

  @Override
  public Optional<String> getArgumentName() {
    return Optional.of("type");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Long> run(CliDependencyManager<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> dependencyManager, Stack<String> arguments) throws Exception {
    final var type = enumFactory.buildFromString(arguments.pop().trim().strip()); 
    final var out = dependencyManager.getStreams().out;
    final ICommand<Long, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> countCommand = new CountCommand(type);
    final ICommandQueue<? extends IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> queue = dependencyManager.getCommandQueue();
    return queue.push(countCommand).doOnNext(result -> {
      out.println("Collection contains " + result.toString() + " elements with type " + type.toString());
    });
  }
}
