package net.whitehorizont.apps.organization_collection_manager.lib;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

public class NumberFactory implements IFromStringBuilder<Integer> {

  @Override
  public Integer buildFromString(String string) throws ValidationError {
    try {
      return Integer.valueOf(string);
    } catch (NumberFormatException e) {
      throw new ValidationError(
          "String \"" + string + "\" is not an integer number.\n"
           + "Expected something like 123.\n"
           + "Number should be within range: " + Integer.MIN_VALUE + " <= number <= " + Integer.MAX_VALUE , e);
    }
  }

}
