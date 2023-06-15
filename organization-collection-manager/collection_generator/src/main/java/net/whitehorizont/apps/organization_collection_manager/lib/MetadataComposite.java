package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
// we know type of node container i.e. TitledNode but now what is inside the container
public class MetadataComposite<ParentHost, Host, WritableHost extends Host, T> extends TitledNode<MetadataComposite<Host, ?, ?, T>, FieldMetadataExtended<Host, WritableHost, ?, T>> implements ICanAcceptVisitor<Host, T> {
  private final Function<ParentHost, WritableHost> hostExtractor;

  public MetadataComposite(String displayedName, List<FieldMetadataExtended<Host, WritableHost, ?, T>> leafs,
      List<MetadataComposite<Host, ?, ?, T>> children, Function<ParentHost, WritableHost> hostExtractor) {
    super(displayedName, leafs, children);
    this.hostExtractor = hostExtractor;
  }

  // fuck that shit
  // without this method java can't track type of child host
  @Override
  public void accept(Host host, IMetadataCompositeVisitor<T> visitor) {
    for (final var leaf : getLeafs()) {
      leaf.accept(host, visitor);
    }

    for (final var child : getChildren()) {
      acceptChild(host, child, visitor);
    }
  }

  private static <Host, Child, T> void acceptChild(Host host, MetadataComposite<Host, Child, ?, T> node, IMetadataCompositeVisitor<T> visitor) {
    final var child = node.extractChildHost(host);
    node.accept(child, visitor);
  } 

  private Host extractChildHost(ParentHost parentHost) {
    return hostExtractor.apply(parentHost);
  }
}