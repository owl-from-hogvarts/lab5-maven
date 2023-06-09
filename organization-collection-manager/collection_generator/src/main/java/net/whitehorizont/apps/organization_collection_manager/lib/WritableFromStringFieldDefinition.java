package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class WritableFromStringFieldDefinition<V> extends WriteableFieldDefinition<V> {
  private IFromStringBuilder<V> builder;

  public WritableFromStringFieldDefinition(FieldMetadataWithValidators<V, ?> metadata, IFromStringBuilder<V> builder, String initialValue) throws ValidationError {
    super(metadata, builder.buildFromString(initialValue));
    this.builder = builder;
  }
  // arguments have different order to resolve ambiguity
  public WritableFromStringFieldDefinition(FieldMetadataWithValidators<V, ?> metadata, V initialValue, IFromStringBuilder<V> builder) throws ValidationError {
    super(metadata, initialValue);
    this.builder = builder;
  }

  public void setValueFromString(String valueString) throws ValidationError {
    try {
      super.setValue(builder.buildFromString(valueString));
    } catch (NullPointerException e) {
      super.setValue(null);
    }
  }

}
