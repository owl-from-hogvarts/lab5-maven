package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ShowCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class Show<Host extends ICollectionElement<Host>> extends BaseElementCommand<Host>
    implements ICliCommand<CollectionCommandReceiver<Host>> {
  public Show(MetadataComposite<?, Host, ?> metadata) {
    super(metadata);
  }

  private static final String DESCRIPTION = "print all collection elements";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(
      CliDependencyManager<? extends CollectionCommandReceiver<Host>> dependencyManager,
      Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
    return Observable.create(subscriber -> {
      final var receiver = dependencyManager.getCollectionReceiver();

        final var show = new ShowCommand<>(receiver);
        final var out = dependencyManager.getStreams().out;
        dependencyManager.getCommandQueue().push(show).subscribe(keyElement -> {
          final var element = keyElement.getValue();
          printFields(element, keyElement.getKey(), out);
        });
        subscriber.onComplete();
    });
  }
}
