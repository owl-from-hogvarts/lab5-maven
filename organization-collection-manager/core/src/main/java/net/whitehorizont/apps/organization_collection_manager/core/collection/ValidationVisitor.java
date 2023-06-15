package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.IMetadataCompositeVisitor;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;

@NonNullByDefault
public class ValidationVisitor<T> implements IMetadataCompositeVisitor<T> {
  private final T validationObject;
  
  public ValidationVisitor(T validationObject) {
    this.validationObject = validationObject;
  }

  @Override
  public <Host, WriteableHost extends Host, V> void visit(
      FieldMetadataExtended<Host, WriteableHost, V, T> fieldMetadata, Host host) throws ValidationError {
        validate(fieldMetadata, host, validationObject);
  }

  private static <Host, V, T> void validate(FieldMetadataExtended<Host, ?, V, T> metadata, Host host, T validationObject) throws ValidationError {
    final var getter = metadata.getValueGetter();
    final var value = getter.apply(host);
    final var validators = metadata.getValidators();

    // crunch; should be redone
    final var nullCheck = metadata.getNullCheckValidator();
    reportValidationError(metadata, nullCheck.validate(value));

    for (final var validator : validators) {
      final var validationResult = validator.validate(value, validationObject);
      reportValidationError(metadata, validationResult);
    }
  }

  private static void reportValidationError(FieldMetadataExtended<?, ?, ?, ?> metadata, ValidationResult<Boolean> validationResult) throws ValidationError {
    if (!validationResult.getResult()) {
      throw new ValidationError(metadata.getDisplayedName() + ": " + validationResult.getDisplayedMessage());
    }
  }

}
