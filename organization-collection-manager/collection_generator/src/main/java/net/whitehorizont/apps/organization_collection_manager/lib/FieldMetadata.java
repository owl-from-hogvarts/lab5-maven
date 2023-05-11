package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
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
    protected Metadata<V, T> clone() throws CloneNotSupportedException {
      return (Metadata<V, T>) super.clone();
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
  @Override
  public List<Validator<V, T>> getValidators() { // ok at this point I don' know what happens. Java is just mad at me I guess
    return metadata.validators;
  }
  public String getOnNullMessage() {
    return metadata.onNullMessage;
  }
  @Override
  public SimpleValidator<V> getNullCheckValidator() {
    return (value) -> {
      return new ValidationResult<Boolean>(value != null, getOnNullMessage());
    };
  }
}
