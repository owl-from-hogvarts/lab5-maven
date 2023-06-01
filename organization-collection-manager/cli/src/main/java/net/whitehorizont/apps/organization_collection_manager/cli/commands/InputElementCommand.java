package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class InputElementCommand extends BaseElementCommand {
  private static final String HINT_PREFIX = "Hint for next field: ";


  private final Retries retries;

  public InputElementCommand(Retries retries) {
    this.retries = retries;
  }

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

  
  protected IWriteableFieldDefinitionNode promptForFields(IWriteableFieldDefinitionNode node, LineReader lineReader, Streams streams) throws ValidationError {
    return promptForFields(node, lineReader, streams, 0);
  }

  private IWriteableFieldDefinitionNode promptForFields(IWriteableFieldDefinitionNode node, LineReader lineReader, Streams streams,
      int nestLevel) throws ValidationError {
    final var fields = node.getWriteableFromStringFields();
    final var out = streams.out;
    final var err = streams.err;

    final String nodeTitle = node.getDisplayedName();
    final String title = isElement(nestLevel) ? prepareNodeTitle(nodeTitle).build() : buildChildNodeTitle(nodeTitle);
    out.println(title);

    for (final var field : fields) {
      final var metadata = field.getMetadata();
      int retriesLeft = this.retries.retries;

      // repeat until succeed
      while (true) {
        printHint(metadata, nestLevel, out);

        final String fieldPrompt = preparePrompt(metadata, nestLevel);

        @Nullable
        String userInput = readUserInput(lineReader, fieldPrompt);

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

    return node;
  }

  protected Streams prepareStreams(CliDependencyManager<?> dependencyManager) {
    final PrintStream voidOutput = new PrintStream(OutputStream.nullOutputStream());
    final var out = dependencyManager.getDisplayPrompts() ? dependencyManager.getStreams().out : voidOutput;
    final Streams streams = new Streams(dependencyManager.getStreams().in, out, dependencyManager.getStreams().err);

    return streams;
  }

  private static String preparePrompt(FieldMetadata<?, ?> metadata, int nestLevel) {
        final String fieldPrompt = metadata.getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR;
        final String fieldPromptPadded = StringHelper.padStart(fieldPrompt,
            computeNestedPadding(nestLevel, fieldPrompt),
            PADDING_SYMBOL);

        return fieldPromptPadded;
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
    String userInput = lineReader.readLine(fieldPrompt).trim().strip();
    // check for null
    if (userInput.length() < 1) {
      userInput = null;
    }

    return userInput;
  }


  protected <CM extends ICollectionManager<?>> CM getCollectionManager(CliDependencyManager<CM> dependencyManager) {
    return dependencyManager.getCollectionManager();
  }

  protected <C extends ICollection<?, ?, ?>> C getCollection(ICollectionManager<C> collectionManager) throws StorageInaccessibleError {
    return collectionManager.getCollection().blockingFirst();
  }
}
