package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ShowCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class Show implements ICliCommand<CliDependencyManager<? extends ICollectionManager<? extends ICollection<?, ? extends ICollectionElement<?>, ?>, ?>>> {
  private static final String DESCRIPTION = "print all collection elements";
  private static final String DEFAULT_DECORATOR = "-";
  private static final int DECORATED_TITLE_WIDTH = 80;
  

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends ICollectionManager<? extends ICollection<?, ? extends ICollectionElement<?>, ?>, ?>> dependencyManager, Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
        return Observable.create(subscriber -> {
          dependencyManager.getCollectionManager().getCollection().subscribe(receivedCollection -> {
            final ICollection<?, ? extends ICollectionElement<?>, ?> collection = receivedCollection;
            final CollectionCommandReceiver<?, ? extends ICollectionElement<?>, ?> receiver = new CollectionCommandReceiver<>(collection);
          
            final var show = new ShowCommand<>(receiver);
            final var out = new PrintStream(dependencyManager.getStreams().out);
            dependencyManager.getCommandQueue().push(show).subscribe(element -> {
              out.println(prepareElementTitle(element.getDisplayedName(), DEFAULT_DECORATOR));

              final var fields = element.getFields();
              for (final var field : fields) {
                final var value = field.getValue() != null ? field.getValue().toString() : "null";
                out.println(field.getMetadata().getDisplayedName() + ": " + value);
              }
            });
  
            subscriber.onComplete();
          });
        });
         

      }

      private static String prepareElementTitle(String title, String decorator) {
        return StringHelper.padBoth(" " + title + " ", DECORATED_TITLE_WIDTH, decorator);
      }
  
}