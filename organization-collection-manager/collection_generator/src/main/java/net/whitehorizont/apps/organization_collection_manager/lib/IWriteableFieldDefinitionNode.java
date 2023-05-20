package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IWriteableFieldDefinitionNode extends IDisplayable {
  Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields();
  Iterable<IWriteableFieldDefinitionNode> getChildren();
}
