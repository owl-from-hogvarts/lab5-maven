package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;
import net.whitehorizont.apps.collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.InfoCommand;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class Info extends BaseElementCommand<CollectionMetadataComputed> implements ICliCommand<CollectionCommandReceiver<?>> {
  public Info(MetadataComposite<?, CollectionMetadataComputed, ?> metadata) {
    super(metadata);
  }

  private static String INFO_DESCRIPTION = "show information about collection itself";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return INFO_DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var receiver = dependencyManager.getCollectionReceiver();
        dependencyManager.getCommandQueue().push(new InfoCommand(receiver)).blockingSubscribe(collectionMetadata -> {
          printFields(collectionMetadata, dependencyManager.getStreams().out);
        });
        return Observable.empty();
      }

}
