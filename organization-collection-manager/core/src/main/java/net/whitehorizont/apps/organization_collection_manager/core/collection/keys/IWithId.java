package net.whitehorizont.apps.organization_collection_manager.core.collection.keys;

import org.eclipse.jdt.annotation.NonNull;

public interface IWithId<ID extends BaseId> {
  @NonNull ID getId();
}
