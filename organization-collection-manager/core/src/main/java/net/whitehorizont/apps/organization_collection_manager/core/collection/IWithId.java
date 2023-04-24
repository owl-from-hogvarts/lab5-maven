package net.whitehorizont.apps.organization_collection_manager.core.collection;

public interface IWithId<ID extends BaseId> {
  ID getId();
}
