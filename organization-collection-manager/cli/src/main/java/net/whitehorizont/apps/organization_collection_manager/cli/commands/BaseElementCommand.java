package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.PrintStream;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.libs.file_system.DecoratedString;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public abstract class BaseElementCommand<Host> {
  protected static final String FIELD_NAME_VALUE_SEPARATOR = ": ";
  protected static final String DEFAULT_DECORATOR = "-";
  protected static final int DECORATED_TITLE_WIDTH = 80;
  protected static final String LEFT_DECORATOR = "< ";
  protected static final String RIGHT_DECORATOR = " >";
  protected static final String PADDING_SYMBOL = " ";
  protected static final String KEY_DECORATION = " >";
  protected static final int PADDING_MULTIPLIER = 2;
  protected static final int INITIAL_NEST_LEVEL = 0;

  protected final MetadataComposite<?, Host, ?> metadata;

  public BaseElementCommand(MetadataComposite<?, Host, ?> metadata) {
    this.metadata = metadata;
  }

  protected static DecoratedString prepareNodeTitle(String title) {
      final var titleDecorator = getDecorator();
      return titleDecorator.setMiddle(title);
  }

  protected static String buildChildNodeTitle(String title, int nestLevel) {
    return StringHelper.padStart(title, computePaddedStringLength(nestLevel, title), PADDING_SYMBOL) + FIELD_NAME_VALUE_SEPARATOR;
  }

  protected static int computePaddedStringLength(int nestLevel, String string) {
    final int paddingSize = nestLevel * PADDING_MULTIPLIER;
    final int paddedStringLength = paddingSize + string.length();

    return paddedStringLength;
  }

  protected static boolean isElement(int nestLevel) {
    return nestLevel == INITIAL_NEST_LEVEL;
  }

  private static DecoratedString getDecorator() {
    final var decorations = new DecoratedString.Decorations()
                                  .leftDecorator(LEFT_DECORATOR)
                                  .rightDecorator(RIGHT_DECORATOR);
    final var decorator = new DecoratedString(DEFAULT_DECORATOR, DECORATED_TITLE_WIDTH,decorations);

    return decorator;
  }

  protected void printFields(Host host, PrintStream out) {
    printFields(metadata, host, Optional.empty(), out, INITIAL_NEST_LEVEL);
  }

  protected void printFields(Host host, ISerializableKey key, PrintStream out) {
    printFields(metadata, host, Optional.of(key), out, INITIAL_NEST_LEVEL);
  }

  private static <Host, WritableHost extends Host> void printFields(MetadataComposite<?, Host, WritableHost> node, Host host, Optional<ISerializableKey> key, PrintStream out, int nestLevel) {
    final String nodeTitle = node.getDisplayedName();
    final var titleDecorated = prepareNodeTitle(nodeTitle);
    if (key.isPresent()) {
      titleDecorated.setLeft(key.get().serialize());
    }

    if (isElement(nestLevel)) {
      out.println(titleDecorated.build());
    }

    final var fields = node.getLeafs();
    for (final var field : fields) {
      final var valueGetter = field.getValueGetter();
      final var value = valueGetter.apply(host) != null ? valueGetter.apply(host).toString() : "null";
      final String fieldNameValue = field.getDisplayedName() + FIELD_NAME_VALUE_SEPARATOR + value;
      final String paddedFieldNameValue = StringHelper.padStart(fieldNameValue, computePaddedStringLength(nestLevel, fieldNameValue), PADDING_SYMBOL);
      out.println(paddedFieldNameValue);
    }

    for (final var child : node.getChildren()) {
      doForChild(child, host, key, out, nestLevel);
    }
  }
  
  private static <ParentHost, Host, WritableHost extends Host> void doForChild(MetadataComposite<ParentHost, Host, WritableHost> childMetadata, ParentHost host, Optional<ISerializableKey> key, PrintStream out, int nestLevel) {
    final var childHost = childMetadata.extractChildHost(host);
    out.println(buildChildNodeTitle(childMetadata.getDisplayedName(), nestLevel));
    printFields(childMetadata, childHost, key, out, nestLevel + 1);
  }
}
