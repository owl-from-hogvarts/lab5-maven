package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class WritableFromStringFieldDefinition<V> extends WriteableFieldDefinition<V> {
  public WritableFromStringFieldDefinition(FieldMetadataWithValidators<V, ?> metadata, IFromStringBuilder<V> builder, String initialValue) throws ValidationError {
    super(metadata, buildValueSafe(builder, initialValue));
  }
  // arguments have different order to resolve ambiguity
  public WritableFromStringFieldDefinition(FieldMetadataWithValidators<V, ?> metadata, V initialValue, IFromStringBuilder<V> builder) throws ValidationError {
    super(metadata, initialValue);
  }

  private static <V> V buildValueSafe(IFromStringBuilder<V> builder, String valueString) throws ValidationError {
    try {
      return builder.buildFromString(valueString);
    } catch (NullPointerException e) {
      return null;
    }
  }

}
