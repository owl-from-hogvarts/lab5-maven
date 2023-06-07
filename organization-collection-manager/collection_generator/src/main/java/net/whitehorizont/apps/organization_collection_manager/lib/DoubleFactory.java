package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class DoubleFactory implements IFromStringBuilder<Double> {

  @Override
  public Double buildFromString(String string) throws ValidationError {
    try {
      return Double.valueOf(string);
    } catch (NumberFormatException e) {
      throw new ValidationError(
          "String \"" + string + "\" is not an double number.\n"
           + "Expected something like 123.456\n"
           + "Number should be within range: " + Double.MIN_VALUE + " <= number <= " + Double.MAX_VALUE , e);
    }
  }
  
}
