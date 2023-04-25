package net.whitehorizont.apps.organization_collection_manager.core.collection;

public interface ICollectionElement<P, I extends BaseId> extends IWithId<I> {
  P getPrototype();
}
