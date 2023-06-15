package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface IMetadataCompositeVisitor<T> {
  <Host, WriteableHost extends Host, V> void visit(FieldMetadataExtended<Host, WriteableHost, V> fieldMetadata, Host host) throws ValidationError;
  <Host, WriteableHost extends Host, V> void visit(FieldMetadataExtendedWithRichValidators<Host, WriteableHost, V, T> fieldMetadata, Host host) throws ValidationError;
}
