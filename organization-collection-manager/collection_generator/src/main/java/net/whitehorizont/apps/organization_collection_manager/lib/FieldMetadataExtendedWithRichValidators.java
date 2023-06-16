package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.IValidatorsProvider;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;

@NonNullByDefault
public class FieldMetadataExtendedWithRichValidators<Host, WritableHost extends Host, V, T> extends FieldMetadataExtended<Host, WritableHost, V> implements  IValidatorsProvider<V, T> {
  private final List<Validator<V, T>> validators;
  
  public FieldMetadataExtendedWithRichValidators(MetadataWithValidators<Host, WritableHost, V, T> metadata) {
    super(metadata);
    this.validators = metadata.validators;
  }

  public static class MetadataWithValidators<Host, WritableHost extends Host, V, T> extends Metadata<MetadataWithValidators<Host, WritableHost, V, T>, Host, WritableHost, V> {
      private final List<Validator<V, T>> validators = new ArrayList<>();

      public MetadataWithValidators<Host, WritableHost, V, T> addValidator(Validator<V, T> validator) {
        this.validators.add(validator);
        return this;
      }

      @Override
      public FieldMetadataExtendedWithRichValidators<Host, WritableHost, V, T> build() {
        return new FieldMetadataExtendedWithRichValidators<>(this);
      }
  }

  public List<Validator<V, T>> getValidators() {
    return validators;
  }
  
}