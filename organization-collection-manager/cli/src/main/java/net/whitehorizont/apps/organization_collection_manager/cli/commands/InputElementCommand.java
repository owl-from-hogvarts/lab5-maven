package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.OutputStream;
import java.io.PrintStream;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class InputElementCommand<Host extends ICollectionElement<Host>, WritableHost extends Host> extends BaseElementCommand<Host> {
  private static final String HINT_PREFIX = "Hint for next field: ";
  // FIXME: the property should be within parent class
  private final MetadataComposite<?, Host, WritableHost> metadata;
  private final IWritableHostFactory<WritableHost> elementFactory;

  final protected WritableHost getWritableCollectionElement() {
    return this.elementFactory.createWritable();
  }

  private final Retries retries;

  public InputElementCommand(MetadataComposite<?, Host, WritableHost> metadata, IWritableHostFactory<WritableHost> elementFactory, Retries retries) {
    super(metadata);
    this.metadata = metadata;
    this.retries = retries;
    this.elementFactory = elementFactory;
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

  
  protected Host promptForFields(WritableHost host, LineReader lineReader, Streams streams) throws ValidationError {
    return promptForFields(this.metadata, host, lineReader, streams, 0);
  }

  private <ParentHost, Host, WritableHost extends Host> Host promptForFields(MetadataComposite<?, Host, WritableHost> node, WritableHost host, LineReader lineReader, Streams streams,
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
    if (isElement(nestLevel)) {
      out.println(prepareNodeTitle(nodeTitle).build());
    }

    for (final var metadata : fieldsMetadata) {
      if (metadata.getTags().contains(Tag.SKIP_INTERACTIVE_INPUT)) {
        continue;
      }
      
      int retriesLeft = this.retries.retries;

      // repeat until succeed
      while (true) {
        printHint(metadata, nestLevel, out);

        final String fieldPrompt = preparePrompt(metadata, nestLevel);

        @Nullable
        String userInput = readUserInput(lineReader, fieldPrompt);

        try {
          setValue(metadata, userInput, host);

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

    for (final var childMetadata : node.getChildren()) {
      doForChild(childMetadata, host, lineReader, streams, nestLevel);
    }
    
    return host;
  }

  private static <Host, WritableHost extends Host, V> void setValue(FieldMetadataExtended<Host, WritableHost, V> metadata, @Nullable String input, WritableHost host) throws ValidationError {
    final var valueBuilder = metadata.getValueBuilder().get();
    // DONE: handle NPE
    final var value = input != null ? valueBuilder.buildFromString(input) : null;

    FieldMetadataExtended.basicCheck(metadata, value);
    metadata.getValueSetter().accept(host, value);
  }

  private <ParentHost, Host, WritableHost extends Host> void doForChild(MetadataComposite<ParentHost, Host, WritableHost> childMetadata, ParentHost host, LineReader lineReader, Streams streams, int nestLevel) throws ValidationError {
      final var childHost = childMetadata.extractChildHost(host);
      streams.out.println(buildChildNodeTitle(childMetadata.getDisplayedName(), nestLevel));
      promptForFields(childMetadata, childHost, lineReader, streams, nestLevel + 1);
  }

  protected Streams prepareStreams(CliDependencyManager<?> dependencyManager) {
    final PrintStream voidOutput = new PrintStream(OutputStream.nullOutputStream());
    final var out = dependencyManager.getDisplayPrompts() ? dependencyManager.getStreams().out : voidOutput;
    final Streams streams = new Streams(dependencyManager.getStreams().in, out, dependencyManager.getStreams().err);

    return streams;
  }

  private static String preparePrompt(FieldMetadataExtended<?, ?, ?> metadata, int nestLevel) {
        final String fieldPrompt = metadata.getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR;
        final String fieldPromptPadded = StringHelper.padStart(fieldPrompt,
            computePaddedStringLength(nestLevel, fieldPrompt),
            PADDING_SYMBOL);

        return fieldPromptPadded;
  }

  private static void printHint(FieldMetadataExtended<?, ?, ?> metadata, int nestLevel, PrintStream out) {
    if (metadata.getHint().isPresent()) {
      final String hint = HINT_PREFIX + metadata.getHint().get();
      final String hintPadded = StringHelper.padStart(hint, computePaddedStringLength(nestLevel, hint), PADDING_SYMBOL);

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
