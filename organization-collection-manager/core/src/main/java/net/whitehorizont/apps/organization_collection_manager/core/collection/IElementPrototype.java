package net.whitehorizont.apps.organization_collection_manager.core.collection;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinition;

public interface IElementPrototype<R> {
  R getRawElementData();
  // @NonNull Iterable<@NonNull WriteableFieldDefinition<?>> getDisplayableFields();
  Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields();
}
