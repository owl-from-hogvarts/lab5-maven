package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.IValidatorsProvider;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.SimpleValidator;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;

@NonNullByDefault
public class FieldMetadataWithValidators<V, T> extends BasicFieldMetadata implements IValidatorsProvider<V, T> {
  private final Metadata<V, T> metadata;

  private FieldMetadataWithValidators(Metadata<V, T> metadata) {
    super(metadata.displayedName);
    this.metadata = metadata;
  }

  public static class Metadata<V, T> {
    private String displayedName = "";
    private String description = "";
    private Optional<String> hint = Optional.empty();
    private Optional<IFromStringBuilder<V>> valueBuilder = Optional.empty();
  
    public Metadata<V, T> setValueBuilder(IFromStringBuilder<V> valueBuilder) {
      this.valueBuilder = Optional.of(valueBuilder);
      return this;
    }

    private Optional<String> onNullMessage = Optional.of("Field should NOT be empty");
    private static final String OK_MESSAGE = "Everything ok!";

    private List<Validator<V, T>> validators = new ArrayList<>();
  

    public Metadata<V, T> setDisplayedName(String displayedName) {
      this.displayedName = displayedName;
      return this;
    }
    public Metadata<V, T> setDescription(String description) {
      this.description = description;
      return this;
    }
    public Metadata<V, T> setRequired(String onNullMessage) {
      this.onNullMessage = Optional.of(onNullMessage);
      return this;
    }
    public Metadata<V, T> setNullable() {
      this.onNullMessage = Optional.empty();
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

    public Metadata<V, T> setHint(String hint) {
      this.hint = Optional.of(hint);
      return this;
    }

    public FieldMetadataWithValidators<V, T> build() {
      return new FieldMetadataWithValidators<>(this);
    }

    @Override
    protected Metadata<V, T> clone() throws CloneNotSupportedException {
      return (Metadata<V, T>) super.clone();
    }
  }

  public Optional<IFromStringBuilder<V>> getValueBuilder() {
    return metadata.valueBuilder;
  }
  
  @Override
  public String getDisplayedName() {
    return metadata.displayedName;
  }
  public String getDescription() {
    return metadata.description;
  }
  public boolean isNullable() {
    return metadata.onNullMessage.isEmpty();
  }
  public Optional<String> getHint() {
    return metadata.hint;
  }
  @Override
  public List<Validator<V, T>> getValidators() { // ok at this point I don' know what happens. Java is just mad at me I guess
    return metadata.validators;
  }
  /** Better check if field is nullable at first */
  public Optional<String> getOnNullMessage() {
    return metadata.onNullMessage;
  }
  @Override
  public SimpleValidator<V> getNullCheckValidator() {
    return (value) -> {
      final String message = isNullable() ? Metadata.OK_MESSAGE : getOnNullMessage().get();
      return new ValidationResult<Boolean>(isNullable() || value != null, message);
      // required isNull
      // f        f    ok
      // f        t    ok
      // t        f    ok
      // t        t    not ok
    };
  }
}
