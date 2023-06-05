package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationType;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CountCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.lib.EnumFactory;

@NonNullByDefault
public class CountByType extends BaseElementCommand implements ICliCommand<OrganisationCollectionCommandReceiver> {
  private static final String DESCRIPTION = "Count element of collection which have type {type}";
  private final EnumFactory<OrganisationType> enumFactory = new EnumFactory<>(OrganisationType.class);

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends OrganisationCollectionCommandReceiver> dependencyManager, Stack<String> arguments) throws Exception {
    final var type = enumFactory.buildFromString(arguments.pop().trim().strip()); 
    final var countCommand = new CountCommand(dependencyManager.getCollectionReceiver(), type);
    final var out = dependencyManager.getStreams().out;
    dependencyManager.getCommandQueue().push(countCommand).blockingSubscribe(result -> {
      out.println("Collection contains " + result.toString() + " elements with type " + type.toString());
    });

    return Observable.empty();
    // prototype.
  }
  
}
