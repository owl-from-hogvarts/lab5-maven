package net.whitehorizont.libs.file_system;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DecoratedString {
  public static final String DEFAULT_LEFT_DECORATOR = "< ";
  public static final String DEFAULT_RIGHT_DECORATOR = " >";
  private static final int MIN_ALLOWED_OFFSET = 0;

  private final String leftDecoration;
  private final String rightDecoration;
  private final String underlying;

  public DecoratedString(String backgroundSymbol, int length, Decorations decorations) {
    assert length > 1;
    this.underlying = backgroundSymbol.repeat(length);
    this.leftDecoration = decorations.leftDecoration.orElse(DEFAULT_LEFT_DECORATOR);
    this.rightDecoration = decorations.rightDecoration.orElse(DEFAULT_RIGHT_DECORATOR);
  }

  public String maskWithDecorations(EPosition position, String overlay) {
    
    return switch (position) {
      case LEFT -> maskWithDecorations(MIN_ALLOWED_OFFSET, overlay);
      case MIDDLE -> maskWithDecorations(StringHelper.computeMiddleStringOffset(underlying.length(), overlay), overlay);
      case RIGHT -> maskWithDecorations(underlying.length() - overlay.length(), overlay);
    };
  }

  public static enum EPosition {
    RIGHT,
    MIDDLE,
    LEFT
  }

  public String maskWithDecorations(int offset, String overlay) {
    assert offset >= MIN_ALLOWED_OFFSET;
    var totalOffset = offset - leftDecoration.length();
    var leftDecoration = this.leftDecoration;
    // omit decoration
    if (totalOffset < MIN_ALLOWED_OFFSET) {
      leftDecoration = leftDecoration.substring(Math.abs(totalOffset), leftDecoration.length());
      totalOffset = MIN_ALLOWED_OFFSET;
    }

    var totalLength = totalOffset + leftDecoration.length() + overlay.length() + this.rightDecoration.length();

    var rightDecoration = this.rightDecoration;
    if (totalLength > underlying.length()) {
      final int difference = totalLength - underlying.length();
      rightDecoration = rightDecoration.substring(0, rightDecoration.length() - difference);
    }

    final var decoratedString = leftDecoration + overlay + rightDecoration;
    return mask(underlying, totalOffset, decoratedString);
  }

  public static final String mask(String underlying, int offset, String overlay) {
    final var underlyingBuilder = new StringBuilder(underlying);
    underlyingBuilder.replace(offset, offset + overlay.length(), overlay);
    return underlyingBuilder.toString();
  }

  public static class Decorations {
    private Optional<String> leftDecoration = Optional.empty();
    private Optional<String> rightDecoration = Optional.empty();

    public Decorations leftDecorator(String decorator) {
      leftDecoration = Optional.of(decorator);
      return this;
    }

    public Decorations rightDecorator(String decorator) {
      rightDecoration = Optional.of(decorator);
      return this;
    }
  }

}
