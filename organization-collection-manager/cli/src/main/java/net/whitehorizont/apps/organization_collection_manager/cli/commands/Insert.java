package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class Insert<P extends IElementPrototype<?>, CM extends ICollectionManager<? extends ICollection<P, ?, ?>, ?>>
    extends BaseElementCommand implements ICliCommand<CliDependencyManager<CM>> {
  private static final String DESCRIPTION = "insert element into collection";

  private static final String HINT_PREFIX = "Hint for next field: ";

  private final Retries retries;
  public static class Retries {
    private final int retries;
    private final boolean isInfinite;

    /** Infinitely ask for field with failed validation */
    public Retries() {
      this.retries = -1;
      this.isInfinite = true;
    }

    public Retries(int retries) {
      assert retries > 0;
      this.retries = retries;
      this.isInfinite = false;
    }
  }

  public Insert(Retries retries) {
    this.retries = retries;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<CM> dependencyManager, Stack<String> arguments)
      throws IOException, StorageInaccessibleError {

    final var collectionManager = dependencyManager.getCollectionManager();
    final ICollection<P, ?, ?> collection = collectionManager.getCollection().blockingFirst();

    final var prototype = collection.getElementPrototype();
    final var lineReader = dependencyManager.getGenericLineReader();

    try {
      final PrintStream voidOutput = new PrintStream(OutputStream.nullOutputStream());
      final var out = dependencyManager.getDisplayPrompts() ? dependencyManager.getStreams().out : voidOutput;
      final Streams streams = new Streams(dependencyManager.getStreams().in, out, dependencyManager.getStreams().err);
      promptForFields(prototype, lineReader, streams);
    } catch (ValidationError e) {
      return Observable.error(e);
    }

    final var collectionReceiver = new CollectionCommandReceiver<>(collection);
    final var insertCommand = new InsertCommand<>(prototype, collectionReceiver);

    return dependencyManager.getCommandQueue().push(insertCommand);
  }

  private void promptForFields(IWriteableFieldDefinitionNode node, LineReader lineReader, Streams streams) throws ValidationError {
    promptForFields(node, lineReader, streams, 0);
  }

  private void promptForFields(IWriteableFieldDefinitionNode node, LineReader lineReader, Streams streams,
      int nestLevel) throws ValidationError {
    final var fields = node.getWriteableFromStringFields();
    final var out = streams.out;
    final var err = streams.err;

    out.println(prepareNodeTitle(node.getDisplayedName(), DEFAULT_DECORATOR, nestLevel));

    for (final var field : fields) {
      final var metadata = field.getMetadata();
      int retriesLeft = this.retries.retries;

      // repeat until succeed
      while (true) {
        printHint(metadata, nestLevel, out);

        // prepare prompt
        final String fieldPrompt = metadata.getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR;
        final String fieldPromptPadded = StringHelper.padStart(fieldPrompt,
            computeNestedPadding(nestLevel, fieldPrompt),
            PADDING_SYMBOL);

        @Nullable
        String userInput = readUserInput(lineReader, fieldPromptPadded);

        try {
          field.setValueFromString(userInput);
          // if successful, get out of here
          // on error next statement will be skipped
          break;
        } catch (ValidationError e) {
          if (retries.isInfinite) {
            err.println(e.getMessage());
            continue;
          }

          retriesLeft -= 1;

          if (retriesLeft <= 0) {
            throw e;
          }

        }
      }
    }

    for (final var child : node.getChildren()) {
      promptForFields(child, lineReader, streams, nestLevel + 1);
    }
  }

  private static void printHint(FieldMetadata<?, ?> metadata, int nestLevel, PrintStream out) {
    if (metadata.getHint().isPresent()) {
      final String hint = HINT_PREFIX + metadata.getHint().get();
      final String hintPadded = StringHelper.padStart(hint, computeNestedPadding(nestLevel, hint), PADDING_SYMBOL);

      out.println(hintPadded);
    }
  }

  private static @Nullable String readUserInput(LineReader lineReader, String fieldPrompt) {
    // read user input
    @Nullable
    String userInput = lineReader.readLine(fieldPrompt).trim();
    // check for null
    if (userInput.length() < 1) {
      userInput = null;
    }

    return userInput;
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
