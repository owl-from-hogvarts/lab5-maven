package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinitionNode;

public interface IElementPrototype<R> extends WriteableFieldDefinitionNode {
  R getRawElementData();
  IElementPrototype<R> setFromRawData(R rawData) throws ValidationError;
  // @NonNull Iterable<@NonNull WriteableFieldDefinition<?>> getDisplayableFields();
  Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields();
}
