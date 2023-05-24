package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class WriteableFieldDefinition<V> extends BaseFieldDefinition<V, FieldMetadata<V, ?>> {

  public WriteableFieldDefinition(FieldMetadata<V, ?> metadata, V initialValue) throws ValidationError {
    super(metadata, initialValue);
  }

  @Override
  public void setValue(V value) throws ValidationError {
    super.setValue(value);
  }

}
