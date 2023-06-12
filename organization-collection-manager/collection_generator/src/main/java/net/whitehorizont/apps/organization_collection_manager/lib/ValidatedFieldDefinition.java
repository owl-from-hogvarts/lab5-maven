package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;

@NonNullByDefault
public class ValidatedFieldDefinition<V, T> extends FieldWithExtendedMetadata<V, FieldMetadataWithValidators<V, T>> {

  @SuppressWarnings("null")
  public ValidatedFieldDefinition(FieldMetadataWithValidators<V, T> metadata, V initValue, T t) throws ValidationError {
    super(metadata, checkValue(metadata, initValue, t));
  }

  private static <V, T> V checkValue(FieldMetadataWithValidators<V, T> metadata, V value, T t) throws ValidationError {
    runValidators(metadata.getValidators(), value, t, metadata);
    return value;
  }

  private static <V, T> void runValidators(Iterable<Validator<V, T>> validators, V value, T t, FieldMetadataWithValidators<V, T> metadata) throws ValidationError {
    for (Validator<V, T> validator : validators) {
      final var validationResult = validator.validate(value, t);
      reportValidationError(validationResult, metadata);
    }
  }

}
