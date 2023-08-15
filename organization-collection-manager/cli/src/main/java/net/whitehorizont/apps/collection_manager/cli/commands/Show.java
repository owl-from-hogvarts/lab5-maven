package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.commands.ShowCommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;

@NonNullByDefault
public class Show<Host extends ICollectionElement<Host>> extends BaseElementCommand<Host>
    implements ICliCommand<IProvideCollectionReceiver<? extends ICollectionCommandReceiver<Host>>> {
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
      CliDependencyManager<IProvideCollectionReceiver<? extends ICollectionCommandReceiver<Host>>> dependencyManager,
      Stack<String> arguments) throws Exception {
    return Observable.create(subscriber -> {
      final var show = new ShowCommand<ICollectionCommandReceiver<Host>, Host>();
      final var out = dependencyManager.getStreams().out;
      dependencyManager.getCommandQueue().push(show).subscribe(keyElement -> {
        final var element = keyElement.getValue();
        printFields(element, keyElement.getKey(), out);
      });
      subscriber.onComplete();
    });
  }
}
