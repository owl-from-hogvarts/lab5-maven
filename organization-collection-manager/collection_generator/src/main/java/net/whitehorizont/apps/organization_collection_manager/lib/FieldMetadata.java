package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public class FieldMetadata<V, T> implements IValidatorsProvider<V, T> {
  private final Metadata<V, T> metadata;

  public FieldMetadata(Metadata<V, T> metadata) {
    this.metadata = metadata;
  }

  public static class Metadata<V, T> {
    private String displayedName = "";
    private String description = "";
  
    private boolean isNullable = false;
    private String onNullMessage = "Field should NOT be empty";

    private List<Validator<V, T>> validators = new ArrayList<>();
  

    public Metadata<V, T> setDisplayedName(String displayedName) {
      this.displayedName = displayedName;
      return this;
    }
    public Metadata<V, T> setDescription(String description) {
      this.description = description;
      return this;
    }
    public Metadata<V, T> setNullable(boolean isNullable) {
      this.isNullable = isNullable;
      return this;
    }
    public Metadata<V, T> setNullable(boolean isNullable, @NonNull String onNullMessage) {
      setNullable(isNullable);
      this.onNullMessage = onNullMessage;
      return this;
    }
    public Metadata<V, T> addValidators(List<Validator<V, T>> validators) {
      this.validators.addAll(validators);
      return this;
    }
    public Metadata<V, T> addValidator(Validator<V, T> validator) {
      this.validators.add(validator);
      
      return this;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  public String getDisplayedName() {
    return metadata.displayedName;
  }
  public String getDescription() {
    return metadata.description;
  }
  public boolean isNullable() {
    return metadata.isNullable;
  }
  public List<Validator<V, T>> getValidators() {
    return metadata.validators;
  }
  public String getOnNullMessage() {
    return metadata.onNullMessage;
  }
  @Override
  public Validator<V, T> getNullCheckValidator() {
    return (value, t) -> {
      return new ValidationResult<Boolean>(value != null, getOnNullMessage());
    };
  }
}
