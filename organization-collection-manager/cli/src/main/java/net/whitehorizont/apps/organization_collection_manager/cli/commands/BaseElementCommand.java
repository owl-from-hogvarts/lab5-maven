package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.PrintStream;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.libs.file_system.DecoratedString;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public abstract class BaseElementCommand {
  protected static final String FIELD_NAME_VALUE_SEPARATOR = ": ";
  protected static final String DEFAULT_DECORATOR = "-";
  protected static final int DECORATED_TITLE_WIDTH = 80;
  protected static final String LEFT_DECORATOR = "< ";
  protected static final String RIGHT_DECORATOR = " >";
  protected static final String PADDING_SYMBOL = " ";
  protected static final String KEY_DECORATION = " >";
  protected static final int PADDING_MULTIPLIER = 2;
  protected static final int INITIAL_NEST_LEVEL = 0;

  protected static DecoratedString prepareNodeTitle(String title) {
      final var titleDecorator = getDecorator();
      return titleDecorator.setMiddle(title);
  }

  protected static String buildChildNodeTitle(String title) {
    return title + FIELD_NAME_VALUE_SEPARATOR;
  }

  protected static int computeNestedPadding(int nestLevel, String string) {
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

  protected static void printFields(TitledNode<ReadonlyField<?>> node, PrintStream out) {
    printFields(node, Optional.empty(), out, INITIAL_NEST_LEVEL);
  }

  protected static void printFields(TitledNode<ReadonlyField<?>> node, ISerializableKey key, PrintStream out) {
    printFields(node, Optional.of(key), out, INITIAL_NEST_LEVEL);
  }

  private static void printFields(TitledNode<ReadonlyField<?>> node, Optional<ISerializableKey> key, PrintStream out, int nestLevel) {
    final String nodeTitle = node.getDisplayedName();
    final var titleDecorated = prepareNodeTitle(nodeTitle);
    if (key.isPresent()) {
      titleDecorated.setLeft(key.get().serialize());
    }
    out.println(isElement(nestLevel) ? titleDecorated.build() : buildChildNodeTitle(nodeTitle));

    final var fields = node.getLeafs();
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
