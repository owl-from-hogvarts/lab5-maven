package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;


@NonNullByDefault
// we know type of node container i.e. TitledNode but now what is inside the container
public class MetadataComposite<ParentHost, Host, WritableHost extends Host, T> extends TitledNode<MetadataComposite<Host, ?, ?, ? super T>, FieldMetadataExtended<Host, WritableHost, ?>> implements ICanValidate<Host, T> {
  private final Function<ParentHost, WritableHost> hostExtractor;

  public MetadataComposite(String displayedName, List<FieldMetadataExtended<Host, WritableHost, ?>> leafs,
      List<MetadataComposite<Host, ?, ?, ? super T>> children, Function<ParentHost, WritableHost> hostExtractor) {
    super(displayedName, leafs, children);
    this.hostExtractor = hostExtractor;
  }

  @Override
  public void validate(Host host, T validationObject) throws ValidationError {
    for (final var leaf : getLeafs()) {
      leaf.validate(host, validationObject);
    }

    for (final var child : getChildren()) {
      validateChild(child, host, validationObject);
    }
  }

  // fuck that shit
  // without this method java can't track type of child host
  private static <Child, Host, T> void validateChild(MetadataComposite<Host, Child, ?, T> node, Host host, T validationObject) throws ValidationError {
    final var child = node.extractChildHost(host);
    node.validate(child, validationObject);
  }

  public void fill(WritableHost to, Host from) {
    for (final var leaf : getLeafs()) {
      leaf.fill(to, from);
    }

    for (final var child : getChildren()) {
      child.fill(to, from);
    }
  }

  public void fill(WritableHost to, Host from, Tag tag) {
    for (final var leaf : getLeafs()) {
      leaf.fill(to, from, tag);
    }

    for (final var child : getChildren()) {
      child.fill(to, from, tag);
    }
  }

  public WritableHost extractChildHost(ParentHost parentHost) {
    return hostExtractor.apply(parentHost);
  }
}
