package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.libs.file_system.DecoratedString;

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
}
