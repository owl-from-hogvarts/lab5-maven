package net.whitehorizont.libs.file_system;

public class StringHelper {
  public static final String padBoth(String string, int targetLength, String padString) {
    // compute amount of remaining symbols
    final var stringLength = string.length();
    final var totalPadding = computeRemainingSpace(stringLength, targetLength);
    // divide it by two to get amount of symbols for each side
    // use this value as left padding
    final var leftPadding = (int) totalPadding / 2;
    
    final String leftPadded = padStart(string, leftPadding + stringLength, padString);
    final String fullPadded = padEnd(leftPadded, targetLength, padString);

    return fullPadded;
  }

  public static final int computeMiddleStringOffset(int availableSpace, String string) {
    return (availableSpace - string.length()) / 2;
  }
  
  public static final String padEnd(String string, int targetLength, String padString) {
    final var padding = computePaddingString(string.length(), targetLength, padString);
    return string + padding;
  }

  public static final String padStart(String string, int targetLength, String padString) {
    final var padding = computePaddingString(string.length(), targetLength, padString);
    return padding + string;
  }

  private static final String computePaddingString(int originalLength, int targetLength, String padString) {
    final var difference = computeRemainingSpace(originalLength, targetLength);

    if (difference <= 0) {
      return "";
    }

    return padString.repeat(difference);
  }

  private static final int computeRemainingSpace(int originalLength, int targetLength) {
    return targetLength - originalLength;
  }
}
