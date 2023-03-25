package net.whitehorizont.apps.organization_collection_manager.lib;



public class FieldDefinition<V, T> {
  private V value;
  private FieldMetadata<V, T> metadata;
  private final T validationObject;

  @SuppressWarnings("null")
  public FieldDefinition(FieldMetadata<V, T> metadata, V initValue, T t) throws ValidationError {
    validationObject = t;
    setValue(initValue, t);
    this.metadata = metadata;
  }

  public FieldMetadata<V, T> getMetadata() {
    return this.metadata;
  }

  /** 
   * Method validates value against validationObject initially passed into constructor
   * 
   * To validate against something different, see {@code setValue(V, T)}
   */
  public FieldDefinition<V, T> setValue(V value) throws ValidationError {
    return setValue(value, validationObject);
  }

  public FieldDefinition<V, T> setValue(V value, T t) throws ValidationError {
    getMetadata().getNullCheckValidator().validate(value, t);
    runValidators(this.getMetadata().getValidators(), value, t);

    this.value = value;
    return this;
  }

  private void runValidators(Iterable<Validator<V, T>> validators, V value, T t) throws ValidationError {
    for (Validator<V, T> validator : validators) {
      ValidationResult<Boolean> validationResult = validator.validate(value, t);
        if (!validationResult.getResult()) {
          throw new ValidationError(this.getMetadata().getDisplayedName() + ": " + validationResult.getDisplayedMessage());
        }
    }
  }
  
  public V getValue() {
    return this.value;
  }

}
