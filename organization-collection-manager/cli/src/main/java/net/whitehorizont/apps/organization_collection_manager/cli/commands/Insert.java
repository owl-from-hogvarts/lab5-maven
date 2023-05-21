package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class Insert<P extends IElementPrototype<?>, CM extends ICollectionManager<? extends ICollection<P, ?, ?>, ?>>
    extends BaseElementCommand implements ICliCommand<CliDependencyManager<CM>> {
  private static final String DESCRIPTION = "insert element into collection";

  private static final String HINT_PREFIX = "Hint for next field: ";

  @Override
  public Observable<Void> run(CliDependencyManager<CM> dependencyManager, Stack<String> arguments)
      throws IOException, StorageInaccessibleError {
    final var collectionManager = dependencyManager.getCollectionManager();

    final ICollection<P, ?, ?> collection = collectionManager.getCollection().blockingFirst();

    final var prototype = collection.getElementPrototype();
    final var lineReader = dependencyManager.getLineReader();
    final var out = new PrintStream(dependencyManager.getStreams().out);

    promptForFields(prototype, lineReader, out);

    final var collectionReceiver = new CollectionCommandReceiver<>(collection);
    final var insertCommand = new InsertCommand<>(prototype, collectionReceiver);

    return dependencyManager.getCommandQueue().push(insertCommand);
  }

  private void promptForFields(IWriteableFieldDefinitionNode node, LineReader lineReader, PrintStream out) {
    promptForFields(node, lineReader, out, 0);
  }

  private void promptForFields(IWriteableFieldDefinitionNode node, LineReader lineReader, PrintStream out, int nestLevel) {
    final var fields = node.getWriteableFromStringFields();
    
    out.println(prepareNodeTitle(node.getDisplayedName(), DEFAULT_DECORATOR, nestLevel));
    
    for (final var field : fields) {
      final var metadata = field.getMetadata();

      final String fieldPrompt = metadata.getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR;
      final String fieldPromptPadded = StringHelper.padStart(fieldPrompt, computeNestedPadding(nestLevel, fieldPrompt), PADDING_SYMBOL);
      
      if (metadata.getHint().isPresent()) {
        final String hint = HINT_PREFIX + metadata.getHint().get();
        final String hintPadded = StringHelper.padStart(hint, computeNestedPadding(nestLevel, hint), PADDING_SYMBOL);

        out.println(hintPadded);
      }

      
      @Nullable
      String userInput = lineReader.readLine(fieldPromptPadded).trim();

      if (userInput.length() < 1) {
        userInput = null;
      }

      try {
        field.setValueFromString(userInput);
      } catch (ValidationError e) {
        final var output = lineReader.getTerminal().writer();
        output.println(e.getMessage());
        output.flush();
      }
    }

    for (final var child : node.getChildren()) {
      promptForFields(child, lineReader, out, nestLevel + 1);
    }
  }

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  // receive streams
  // get collection we operating on
  // on that collection get element prototype
  // for each field of element prototype:
  // if field contains children
  // ...
  // if not, retrieve field's metadata.
  // with that metadata (field name especially) ask for input
  // how prompt knows witch type to return?
  // We create driver for that prompt, witch maps known types to prompt calls
}
