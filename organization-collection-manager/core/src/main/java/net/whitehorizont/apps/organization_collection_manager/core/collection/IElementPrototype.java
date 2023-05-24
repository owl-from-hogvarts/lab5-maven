package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

public interface IElementPrototype<R> extends IWriteableFieldDefinitionNode {
  R getRawElementData();
  IElementPrototype<R> setFromRawData(R rawData) throws ValidationError;
  // @NonNull Iterable<@NonNull WriteableFieldDefinition<?>> getDisplayableFields();
}
