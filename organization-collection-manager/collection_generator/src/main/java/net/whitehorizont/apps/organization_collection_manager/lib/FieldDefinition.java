package net.whitehorizont.apps.organization_collection_manager.lib;



public class FieldDefinition<V, T> {
  private V value;
  private FieldMetadata<V, T> metadata;

  @SuppressWarnings("null")
  public FieldDefinition(FieldMetadata<V, T> metadata, V initValue, T t) throws ValidationError {
    this.metadata = metadata;
    setValue(initValue, t);
  }

  public FieldMetadata<V, T> getMetadata() {
    return this.metadata;
  }

  private FieldDefinition<V, T> setValue(V value, T t) throws ValidationError {
    reportValidationError(getMetadata().getNullCheckValidator().validate(value, t));
    runValidators(this.getMetadata().getValidators(), value, t);

    this.value = value;
    return this;
  }

  private void runValidators(Iterable<Validator<V, T>> validators, V value, T t) throws ValidationError {
    for (Validator<V, T> validator : validators) {
      ValidationResult<Boolean> validationResult = validator.validate(value, t);
      reportValidationError(validationResult);
    }
  }

  private void reportValidationError(ValidationResult<Boolean> validationResult) throws ValidationError {
    if (!validationResult.getResult()) {
      throw new ValidationError(this.getMetadata().getDisplayedName() + ": " + validationResult.getDisplayedMessage());
    }
  }
  
  public V getValue() {
    return this.value;
  }

}
