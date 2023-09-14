package net.whitehorizont.apps.collection_manager.cli.errors;

import java.io.PrintStream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class GlobalErrorHandler implements IGlobalErrorHandler {
  private static final String UNKNOWN_ERROR_MESSAGE = "Unknown error ocurred! Please, file new issue on https://github.com/owl-from-hogvarts/lab5-maven";
  private static final String UNKNOWN_ERROR_PRELUDE = "Unknown error";
  private static final String ERROR_PRELUDE = "Error";
  private static final String ERROR_PRELUDE_SEPARATOR = ": ";
  private static final int START_NEST_LEVEL = 0;
  private static final String PADDING_SYMBOL = " ";
  private static final int PADDING_MULTIPLIER = 2;
  private static final String NEW_LINE_SEPARATOR = "\n";

  private static int computePaddedStringLength(int nestLevel, String string) {
    final int paddingSize = nestLevel * PADDING_MULTIPLIER;
    final int paddedStringLength = paddingSize + string.length();

    return paddedStringLength;
  }

  public static void defaultGlobalErrorHandler(Thread thread, Throwable e) {
    defaultGlobalErrorHandler(e, System.err);
  }

  private static boolean isErrorMessageEmpty(@Nullable Throwable error) {
    return error == null || error.getMessage() == null || error.getMessage().length() < 1;
  }

  private static boolean isUnknownError(Throwable error) {
    return error instanceof RuntimeException;
  }

  private static String buildErrorPrefix(boolean isUnknown) {
    return (isUnknown ? UNKNOWN_ERROR_PRELUDE : ERROR_PRELUDE) + ERROR_PRELUDE_SEPARATOR;
  }

  private static void printErrorMessage(PrintStream stream, String message) {
    stream.println(message);
  }

  private static String retrieveErrorMessage(Throwable e, int nestLevel) {
    // won't loos runtime exceptions which are causes of other exceptions
    // may help for debugging
    final boolean shouldPrintUnknownErrorMessage = (isUnknownError(e) && nestLevel == START_NEST_LEVEL)
        || isErrorMessageEmpty(e);
    final @Nullable String messageMaybe = shouldPrintUnknownErrorMessage ? UNKNOWN_ERROR_MESSAGE : e.getMessage();
    return messageMaybe != null ? messageMaybe : e.toString();
  }

  public static boolean defaultGlobalErrorHandler(final Throwable originalException, PrintStream err) {
    int nestLevel = START_NEST_LEVEL;
    Throwable e = originalException;

    // nestLevel check to not cause infinite loop in case of circular reference
    // could use Set, but too complex here
    while (e != null && nestLevel < 5) {
      final String messagePrefix = buildErrorPrefix(isUnknownError(e));
      final String message = retrieveErrorMessage(e, nestLevel);

      // get error message
      // split into lines
      // to first line add nest padding and error prefix
      // to all next lines add nest padding and error prefix length

      final int nestLevelLoop = nestLevel;
      final var firstLineStream = message.lines()
          .findFirst()
          .map(firstLine -> messagePrefix + firstLine)
          .map(firstLine -> StringHelper.padStart(firstLine, computePaddedStringLength(nestLevelLoop, firstLine),
              PADDING_SYMBOL))
          .get();
      final var remainingLines = message.lines()
          .skip(1)
          .map(line -> StringHelper.padStart(line,
              computePaddedStringLength(nestLevelLoop, line) + messagePrefix.length(), PADDING_SYMBOL));

      final String messagePadded = firstLineStream + remainingLines.reduce("", (a, b) -> a + NEW_LINE_SEPARATOR + b);
      printErrorMessage(err, messagePadded);

      e = e.getCause();
      nestLevel++;
    }

    return false;
  }

  public boolean handle(Throwable e, CliDependencyManager<?> dependencyManager) {
    final var err = dependencyManager.getStreams().err;
    return defaultGlobalErrorHandler(e, err);
  }

}
