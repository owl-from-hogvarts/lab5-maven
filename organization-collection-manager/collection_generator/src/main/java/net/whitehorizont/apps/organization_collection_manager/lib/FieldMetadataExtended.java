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
public class FieldMetadataExtended<Host, WritableHost extends Host, V> extends BasicFieldMetadata implements ICanAcceptVisitor<Host, Object> {
  private final Metadata<?, Host, WritableHost, V> metadata;

  protected FieldMetadataExtended(Metadata<?, Host, WritableHost, V> metadata) {
    super(metadata.displayedName);
    this.metadata = metadata.clone();
  }

  public static <Host, WritableHost extends Host, V> Metadata<?, Host, WritableHost, V> builder() {
    return new Metadata<>();
  }

   public static class Metadata<This extends Metadata<This, Host, WritableHost, V>, Host, WritableHost extends Host, V> {
    private String displayedName = "";
    private String description = "";
    private Optional<String> hint = Optional.empty();
    private Optional<IFromStringBuilder<V>> valueBuilder = Optional.empty();
    private BiConsumer<WritableHost, V> valueSetter;
    private Function<Host, V> valueGetter;
    private EnumSet<Tag> tags = EnumSet.noneOf(Tag.class);
  
    @SuppressWarnings("unchecked")
    private This self() {
      return (This) this;
    }

    public This setValueGetter(Function<Host, V> valueGetter) {
      this.valueGetter = valueGetter;
      return self();
    }

    public This addTag(Tag tag) {
      this.tags.add(tag);
      return self();
    }

    public This setValueBuilder(IFromStringBuilder<V> valueBuilder) {
      this.valueBuilder = Optional.of(valueBuilder);
      return self();
    }

    public This setValueSetter(BiConsumer<WritableHost, V> valueSetter) {
      this.valueSetter = valueSetter;
      return self();
    }

    private Optional<String> onNullMessage = Optional.of("Field should NOT be empty");
    private static final String OK_MESSAGE = "Everything ok!";

    private List<SimpleValidator<V>> simpleValidators = new ArrayList<>();
  
    public This setDisplayedName(String displayedName) {
      this.displayedName = displayedName;
      return self();
    }
    public This setDescription(String description) {
      this.description = description;
      return self();
    }
    public This setRequired(String onNullMessage) {
      this.onNullMessage = Optional.of(onNullMessage);
      return self();
    }
    public This setNullable() {
      this.onNullMessage = Optional.empty();
      return self();
    }
    public This addSimpleValidators(List<SimpleValidator<V>> validators) {
      this.simpleValidators.addAll(validators);
      return self();
    }
    public This addSimpleValidator(SimpleValidator<V> validator) {
      this.simpleValidators.add(validator);
      
      return self();
    }

    public This setHint(String hint) {
      this.hint = Optional.of(hint);
      return self();
    }

    public FieldMetadataExtended<Host, WritableHost, V> build() {
      return new FieldMetadataExtended<Host, WritableHost, V>(this);
    }

    @Override
    protected This clone() {
      try {
        return (This) super.clone();
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
  public List<SimpleValidator<V>> getSimpleValidators() {
    return metadata.simpleValidators;
  }
  /** Better check if field is nullable at first */
  public Optional<String> getOnNullMessage() {
    return metadata.onNullMessage;
  }

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
  public void accept(Host host, IMetadataCompositeVisitor<?> visitor) throws Exception {
    visitor.visit(this, host);
  }
}
