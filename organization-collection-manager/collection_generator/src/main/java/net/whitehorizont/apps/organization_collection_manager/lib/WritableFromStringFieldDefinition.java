package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class WritableFromStringFieldDefinition<V> extends WriteableFieldDefinition<V> {
  private IFromStringBuilder<V> builder;

  public WritableFromStringFieldDefinition(FieldMetadata<V, ?> metadata, IFromStringBuilder<V> builder, String initialValue) throws ValidationError {
    super(metadata, builder.buildFromString(initialValue));
    this.builder = builder;
  }

  public void setValue(String value) throws ValidationError {
    super.setValue(builder.buildFromString(value));
  }

}