package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtendedWithRichValidators;
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
      FieldMetadataExtendedWithRichValidators<Host, WriteableHost, V, T> fieldMetadata, Host host) throws ValidationError {
        validate(fieldMetadata, host, validationObject);
  }

  private static <Host, V, T> void validate(FieldMetadataExtendedWithRichValidators<Host, ?, V, T> metadata, Host host, T validationObject) throws ValidationError {
    final var value = getValue(metadata, host);
    final var validators = metadata.getValidators();

    basicCheck(metadata, host);

    for (final var validator : validators) {
      final var validationResult = validator.validate(value, validationObject);
      reportValidationError(metadata, validationResult);
    }
  }

  private static <Host, V> void basicCheck(FieldMetadataExtended<Host, ?, V> metadata, Host host) throws ValidationError {
    final var value = getValue(metadata, host);

    final var nullCheck = metadata.getNullCheckValidator();
    reportValidationError(metadata, nullCheck.validate(value));

    final var simpleValidators = metadata.getSimpleValidators();
    for (final var validator : simpleValidators) {
      final var validationResult = validator.validate(value);
      reportValidationError(metadata, validationResult);
    }
  }

  private static <Host, V> V getValue(FieldMetadataExtended<Host, ?, V> metadata, Host host) {
    final var getter = metadata.getValueGetter();
    final var value = getter.apply(host);
    return value;
  }
  
  private static void reportValidationError(FieldMetadataExtended<?, ?, ?> metadata, ValidationResult<Boolean> validationResult) throws ValidationError {
    if (!validationResult.getResult()) {
      throw new ValidationError(metadata.getDisplayedName() + ": " + validationResult.getDisplayedMessage());
    }
  }

  @Override
  public <Host, WriteableHost extends Host, V> void visit(FieldMetadataExtended<Host, WriteableHost, V> fieldMetadata,
      Host host) throws ValidationError {
        basicCheck(fieldMetadata, host);
      }

}
