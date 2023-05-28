package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ClearCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;

@NonNullByDefault
public class Clear implements ICliCommand {
  private static final String DESCRIPTION = "delete all elements from collection";

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public <DM extends CliDependencyManager<?>> Observable<Void> run(DM dependencyManager, Stack<String> arguments) throws Exception {
    final var collection = dependencyManager.getCollectionManager().getCollection().blockingFirst();
    final var collectionReceiver = new CollectionCommandReceiver<>(collection);
    final var clear = new ClearCommand(collectionReceiver);

    return dependencyManager.getCommandQueue().push(clear);
  }
  
}
