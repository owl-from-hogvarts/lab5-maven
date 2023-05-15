package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;

public interface IElementPrototype<R> {
  R getRawElementData();
  IElementPrototype<R> setFromRawData(R rawData) throws ValidationError;
  // @NonNull Iterable<@NonNull WriteableFieldDefinition<?>> getDisplayableFields();
  Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields();
}
