package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public abstract class BaseElementCommand {
  protected static final String FIELD_NAME_VALUE_SEPARATOR = ": ";
  protected static final String DEFAULT_DECORATOR = "-";
  protected static final String PADDING_SYMBOL = " ";
  protected static final int PADDING_MULTIPLIER = 2;
  protected static final int DECORATED_TITLE_WIDTH = 80;

  protected static String prepareNodeTitle(String title, String decorator, boolean isElement) {
    if (isElement) {
      return StringHelper.padBoth(" " + title + " ", DECORATED_TITLE_WIDTH, decorator);
    }

    return title + FIELD_NAME_VALUE_SEPARATOR;
  }

  protected static int computeNestedPadding(int nestLevel, String string) {
    final int paddingSize = nestLevel * PADDING_MULTIPLIER;
    final int paddedStringLength = paddingSize + string.length();

    return paddedStringLength;
  }

  protected static boolean isElement(int nestLevel) {
    return nestLevel == 0;
  }
}
