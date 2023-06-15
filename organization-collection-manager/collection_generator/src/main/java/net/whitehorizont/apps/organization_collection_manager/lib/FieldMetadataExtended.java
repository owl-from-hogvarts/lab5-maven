package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.IValidatorsProvider;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.SimpleValidator;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;

@NonNullByDefault
public class FieldMetadataExtended<Host, WritableHost extends Host, V, T> extends BasicFieldMetadata implements IValidatorsProvider<V, T>, ICanAcceptVisitor<Host> {
  private final Metadata<Host, WritableHost, V, T> metadata;

  private FieldMetadataExtended(Metadata<Host, WritableHost, V, T> metadata) {
    super(metadata.displayedName);
    this.metadata = metadata.clone();
  }

  public static class Metadata<Host, WritableHost extends Host, V, T> {
    private String displayedName = "";
    private String description = "";
    private Optional<String> hint = Optional.empty();
    private Optional<IFromStringBuilder<V>> valueBuilder = Optional.empty();
    private BiConsumer<WritableHost, V> valueSetter;
    private Function<Host, V> valueGetter;
    private EnumSet<Tag> tags = EnumSet.noneOf(Tag.class);
  
    public Metadata<Host, WritableHost, V, T> setValueGetter(Function<Host, V> valueGetter) {
      this.valueGetter = valueGetter;
      return this;
    }

    public Metadata<Host, WritableHost, V, T> addTag(Tag tag) {
      this.tags.add(tag);
      return this;
    }

    public Metadata<Host, WritableHost, V, T> setValueBuilder(IFromStringBuilder<V> valueBuilder) {
      this.valueBuilder = Optional.of(valueBuilder);
      return this;
    }

    public Metadata<Host, WritableHost, V, T> setValueSetter(BiConsumer<WritableHost, V> valueSetter) {
      this.valueSetter = valueSetter;
      return this;
    }

    private Optional<String> onNullMessage = Optional.of("Field should NOT be empty");
    private static final String OK_MESSAGE = "Everything ok!";

    private List<Validator<V, T>> validators = new ArrayList<>();
  
    public Metadata<Host, WritableHost, V, T> setDisplayedName(String displayedName) {
      this.displayedName = displayedName;
      return this;
    }
    public Metadata<Host, WritableHost, V, T> setDescription(String description) {
      this.description = description;
      return this;
    }
    public Metadata<Host, WritableHost, V, T> setRequired(String onNullMessage) {
      this.onNullMessage = Optional.of(onNullMessage);
      return this;
    }
    public Metadata<Host, WritableHost, V, T> setNullable() {
      this.onNullMessage = Optional.empty();
      return this;
    }
    public Metadata<Host, WritableHost, V, T> addValidators(List<Validator<V, T>> validators) {
      this.validators.addAll(validators);
      return this;
    }
    public Metadata<Host, WritableHost, V, T> addValidator(Validator<V, T> validator) {
      this.validators.add(validator);
      
      return this;
    }

    public Metadata<Host, WritableHost, V, T> setHint(String hint) {
      this.hint = Optional.of(hint);
      return this;
    }

    public FieldMetadataExtended<Host, WritableHost, V, T> build() {
      return new FieldMetadataExtended<>(this);
    }

    @Override
    protected Metadata<Host, WritableHost, V, T> clone() {
      try {
        return (Metadata<Host, WritableHost, V, T>) super.clone();
      } catch (CloneNotSupportedException _ignore) {
        // hope that will never happen
        assert false;
        throw new RuntimeException();
      }
    }
  }

  public BiConsumer<WritableHost, V> getValueSetter() {
    return this.metadata.valueSetter;
  }

  public Function<Host, V> getValueGetter() {
    return this.metadata.valueGetter;
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

  @Override
  public void fill(WritableHost base, Host other) {
    getValueSetter().accept(base, getValueGetter().apply(other));
  }

  @Override
  public void fill(WritableHost base, Host other, Tag tag) {
    if (this.metadata.tags.contains(tag)) {
      fill(base, other);
    }
  }

  public static enum Tag {
    UPDATABLE,
  }

  @Override
  public void accept(Host host, IMetadataCompositeVisitor visitor) {
    visitor.visit(this, host);
  }
}
