package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface WriteableFieldDefinitionNode extends IDisplayable {
  Iterable<WriteableFieldDefinitionNode> getChildren();
}
