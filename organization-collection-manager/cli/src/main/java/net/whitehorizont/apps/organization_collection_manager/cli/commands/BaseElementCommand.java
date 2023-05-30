package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
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

  protected static String prepareNodeTitle(String title, String decorator, int nestLevel) {
    if (isElement(nestLevel)) {
      final var underlyingDecoration = decorator.repeat(DECORATED_TITLE_WIDTH);
      final var middleOffset = StringHelper.computeMiddleStringOffset(underlyingDecoration.length(), title);
      return StringHelper.maskWithDecorations(underlyingDecoration, middleOffset, title, LEFT_DECORATOR, RIGHT_DECORATOR);

    }

    return title + FIELD_NAME_VALUE_SEPARATOR;
  }

  protected static String prepareNodeTitle(ISerializableKey key, String title, String decorator, int nestLevel) {
    final var titleDecorated = prepareNodeTitle(title, decorator, nestLevel);
    if (isElement(nestLevel)) {
      return StringHelper.maskWithDecorations(titleDecorated, 0, key.serialize(), LEFT_DECORATOR, RIGHT_DECORATOR);
    }
    return titleDecorated;
  }

  protected static int computeNestedPadding(int nestLevel, String string) {
    final int paddingSize = nestLevel * PADDING_MULTIPLIER;
    final int paddedStringLength = paddingSize + string.length();

    return paddedStringLength;
  }

  private static boolean isElement(int nestLevel) {
    return nestLevel == INITIAL_NEST_LEVEL;
  }
}
