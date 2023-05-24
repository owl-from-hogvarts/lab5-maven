package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.ArrayList;
import java.util.List;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

public class EnumFactory<E extends Enum<E>> implements IFromStringBuilder<E> {

  private final Class<E> enumClass;

  public EnumFactory(Class<E> e) {
    enumClass = e;
  }

  @Override
  public E buildFromString(String string) throws ValidationError {
    try {
      return Enum.valueOf(enumClass, string);
    } catch (IllegalArgumentException e) {
      throw new ValidationError(buildErrorMessage(string), e);
    }
  }

  private String buildErrorMessage(String input) {
    final List<String> allowedValues = new ArrayList<>();
    for (final var value : enumClass.getEnumConstants()) {
      allowedValues.add(value.toString());
    }

    return "input can be exactly one of following:\n" 
      + String.join("\n", allowedValues) 
      + "\nGot " + input;
  }

}
