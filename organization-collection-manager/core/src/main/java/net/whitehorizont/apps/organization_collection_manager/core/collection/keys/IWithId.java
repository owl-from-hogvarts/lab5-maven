package net.whitehorizont.apps.organization_collection_manager.core.collection.keys;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IWithId<ID extends BaseId> {
  ID getId();
}
