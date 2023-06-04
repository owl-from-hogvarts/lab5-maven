package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataWithValidators;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.Node;
import net.whitehorizont.apps.organization_collection_manager.lib.RawField;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
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

  
  protected Node promptForFields(TitledNode<FieldMetadata<?, ?>> node, LineReader lineReader, Streams streams) throws ValidationError {
    return promptForFields(node, lineReader, streams, 0);
  }

  private Node promptForFields(TitledNode<FieldMetadata<?, ?>> node, LineReader lineReader, Streams streams,
      int nestLevel) throws ValidationError {
        // node contains metadata
        // if node has value builder, prompt user
          // receive user input as string
          // build RawField -> use value builder 
          // to convert user input (string) to a value
          // init RawField with obtained value
          // add node to list
        // if not then skip
        // after all leafs were proceeded
        // proceed with children
        // when complete with children
        // build and return Node<RawField> (populate with computed leafs and children)
        // The resulting tree is known to contain syntactically
        // valid data. It may be directly sent server
    final var fieldsMetadata = node.getLeafs().stream().filter(metadata -> metadata.getValueBuilder().isPresent()).toList();
    final var out = streams.out;
    final var err = streams.err;

    final String nodeTitle = node.getDisplayedName();
    final String title = isElement(nestLevel) ? prepareNodeTitle(nodeTitle).build() : buildChildNodeTitle(nodeTitle);
    out.println(title);

    final var fields = new ArrayList<>(fieldsMetadata.size());

    for (final var metadata : fieldsMetadata) {
      int retriesLeft = this.retries.retries;

      // repeat until succeed
      while (true) {
        printHint(metadata, nestLevel, out);

        final String fieldPrompt = preparePrompt(metadata, nestLevel);

        @Nullable
        String userInput = readUserInput(lineReader, fieldPrompt);

        try {
          final var valueBuilder = metadata.getValueBuilder().get();
          new WritableFromStringFieldDefinition(metadata, valueBuilder, userInput).ge;
          // FIXME: handle NPE
          fields.add(new RawField<>(value));

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

    final List<Node> children = new ArrayList<>();

    for (final var childMetadata : node.getChildren()) {
      final var child = promptForFields(childMetadata, lineReader, streams, nestLevel + 1);
      children.add(child);
    }
    
    return new Node(fields.toArray(), children.toArray(new Node[]{}));
  }

  protected Streams prepareStreams(CliDependencyManager<?> dependencyManager) {
    final PrintStream voidOutput = new PrintStream(OutputStream.nullOutputStream());
    final var out = dependencyManager.getDisplayPrompts() ? dependencyManager.getStreams().out : voidOutput;
    final Streams streams = new Streams(dependencyManager.getStreams().in, out, dependencyManager.getStreams().err);

    return streams;
  }

  private static String preparePrompt(FieldMetadataWithValidators<?, ?> metadata, int nestLevel) {
        final String fieldPrompt = metadata.getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR;
        final String fieldPromptPadded = StringHelper.padStart(fieldPrompt,
            computeNestedPadding(nestLevel, fieldPrompt),
            PADDING_SYMBOL);

        return fieldPromptPadded;
  }

  private static void printHint(FieldMetadataWithValidators<?, ?> metadata, int nestLevel, PrintStream out) {
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
}
