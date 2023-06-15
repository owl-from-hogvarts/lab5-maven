package net.whitehorizont.apps.organization_collection_manager.lib;

public interface ICanAcceptVisitor<Host, T> {
  void accept(Host host, IMetadataCompositeVisitor<? extends T> visitor) throws Exception;
}
