package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;

@NonNullByDefault
public abstract class FieldWithExtendedMetadata<V, M extends FieldMetadataWithValidators<V, ?>> extends ReadonlyField<V> {
  // shadow to eliminate nullness
  // private V value;
  private M metadata;

  @SuppressWarnings("null")
  public FieldWithExtendedMetadata(M metadata, V initialValue) throws ValidationError {
    super(metadata);
    this.metadata = metadata;
    setValue(initialValue);
  }

  public M getMetadata() {
    return this.metadata;
  }

  protected void setValue(V value) throws ValidationError {
    checkPrimitive(value);
    this.value = value;
  }

  /**
   * Performs primitive checks like nullness check
   * 
   * @throws ValidationError
   */
  protected void checkPrimitive(V value) throws ValidationError {
    final var metadata = getMetadata();
    reportValidationError(metadata.getNullCheckValidator().validate(value), metadata);
  }

  protected static <M extends FieldMetadataWithValidators<?, ?>> void reportValidationError(ValidationResult<Boolean> validationResult, M metadata) throws ValidationError {
    if (!validationResult.getResult()) {
      throw new ValidationError(metadata.getDisplayedName() + ": " + validationResult.getDisplayedMessage());
    }
  }

  @Override
  public @Nullable V getValue() {
    return value;
  }
}
