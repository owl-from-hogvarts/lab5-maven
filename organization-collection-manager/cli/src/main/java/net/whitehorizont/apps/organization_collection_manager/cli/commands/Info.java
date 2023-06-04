package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InfoCommand;

@NonNullByDefault
public class Info extends BaseElementCommand implements ICliCommand {
  private static String INFO_DESCRIPTION = "show information about collection itself";

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return INFO_DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var collectionManager = getCollectionManager(dependencyManager);
        final var collection = getCollection(collectionManager);
        final var receiver = new CollectionCommandReceiver<>(collection);
        dependencyManager.getCommandQueue().push(new InfoCommand(receiver)).blockingSubscribe();
        return Observable.empty();
      }

}
