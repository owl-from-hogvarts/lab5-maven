package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IReadonlyTreeProvider extends IDisplayable {
  TitledNode<ReadonlyField<?>> getTree();
}
