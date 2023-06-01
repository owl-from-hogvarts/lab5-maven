package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.ShowCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class Show extends BaseElementCommand 
    implements ICliCommand {
  private static final String DESCRIPTION = "print all collection elements";

  @Override
  public boolean hasArgument() {
    return false;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public <DM extends CliDependencyManager<?>> Observable<Void> run(
      DM dependencyManager,
      Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
    return Observable.create(subscriber -> {
      dependencyManager.getCollectionManager().getCollection().subscribe(receivedCollection -> {
        final var collection = receivedCollection;
        final var receiver = new CollectionCommandReceiver<>(
            collection);

        final var show = new ShowCommand<>(receiver);
        final var out = dependencyManager.getStreams().out;
        dependencyManager.getCommandQueue().push(show).subscribe(keyElement -> {
          final var element = keyElement.getValue();
          printFields(element, keyElement.getKey(), out);
        });

        subscriber.onComplete();
      });
    });
  }



  private static void printFields(IFieldDefinitionNode node, ISerializableKey key, PrintStream out) {
    printFields(node, key, out, INITIAL_NEST_LEVEL);
  }

  private static void printFields(IFieldDefinitionNode node, ISerializableKey key, PrintStream out, int nestLevel) {
    final String nodeTitle = node.getDisplayedName();
    final var titleDecorated = prepareNodeTitle(nodeTitle);
    titleDecorated.setLeft(key.serialize());
    out.println(isElement(nestLevel) ? titleDecorated.build() : buildChildNodeTitle(nodeTitle));

    final var fields = node.getFields();
    for (final var field : fields) {
      final var value = field.getValue() != null ? field.getValue().toString() : "null";
      final String fieldNameValue = field.getMetadata().getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR + value;
      final String paddedFieldNameValue = StringHelper.padStart(fieldNameValue, computeNestedPadding(nestLevel, fieldNameValue), PADDING_SYMBOL);
      out.println(paddedFieldNameValue);
    }

    for (final var child : node.getChildren()) {
      printFields(child, key, out, nestLevel + 1);
    }
  }
}
