package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class WriteableFieldDefinition<V> extends FieldWithExtendedMetadata<V, FieldMetadataExtended<V, ?>> {

  public WriteableFieldDefinition(FieldMetadataExtended<V, ?> metadata, V initialValue) throws ValidationError {
    super(metadata, initialValue);
  }

  @Override
  public void setValue(V value) throws ValidationError {
    super.setValue(value);
  }

}
