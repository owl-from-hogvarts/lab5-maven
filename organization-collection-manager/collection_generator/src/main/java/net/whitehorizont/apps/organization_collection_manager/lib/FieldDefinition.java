package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;

@NonNullByDefault
public class FieldDefinition<V, T> extends BaseFieldDefinition<V, FieldMetadata<V, T>> {

  @SuppressWarnings("null")
  public FieldDefinition(FieldMetadata<V, T> metadata, V initValue, T t) throws ValidationError {
    super(metadata, checkValue(metadata, initValue, t));
  }

  private static <V, T> V checkValue(FieldMetadata<V, T> metadata, V value, T t) throws ValidationError {
    runValidators(metadata.getValidators(), value, t, metadata);
    return value;
  }

  private static <V, T> void runValidators(Iterable<Validator<V, T>> validators, V value, T t, FieldMetadata<V, T> metadata) throws ValidationError {
    for (Validator<V, T> validator : validators) {
      final var validationResult = validator.validate(value, t);
      reportValidationError(validationResult, metadata);
    }
  }

}
