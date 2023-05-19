package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IFieldDefinitionNode extends IDisplayable {
  Iterable<FieldDefinition<?, ?>> getFields();
  Iterable<IFieldDefinitionNode> getChildren();
}
