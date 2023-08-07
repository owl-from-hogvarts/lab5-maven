package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;


/** 
 * Provides set of operations performed on whole host object
 * 
 * This class encapsulates metadata tree traversal logic.
 * 
 * @param <ParentHost> parent context object
 * @param <Host> current context object
 * @param <WritableHost> 
 * @param <T> Validation object
 */
@NonNullByDefault
// we know type of node container i.e. TitledNode but now what is inside of it
public class MetadataComposite<ParentHost, Host, WritableHost extends Host > extends TitledNode<MetadataComposite<Host, ?, ?>, FieldMetadataExtended<Host, WritableHost, ?>> implements IElementInfoProvider<Host>, ICanFill<Host, WritableHost> {
  private final Function<ParentHost, WritableHost> hostExtractor;

  public MetadataComposite(String displayedName, List<FieldMetadataExtended<Host, WritableHost, ?>> leafs,
      List<MetadataComposite<Host, ?, ?>> children, Function<ParentHost, WritableHost> hostExtractor) {
    super(displayedName, leafs, children);
    this.hostExtractor = hostExtractor;
  }

  @Override
  public void validate(Host host) throws ValidationError {
    for (final var leaf : getLeafs()) {
      leaf.validate(host);
    }

    for (final var child : getChildren()) {
      validateChild(child, host);
    }
  }

  // fuck that shit
  // without this method java can't track type of child host
  private static <Child, Host, T> void validateChild(MetadataComposite<Host, Child, ?> node, Host host) throws ValidationError {
    final var child = node.extractChildHost(host);
    node.validate(child);
  }

  public void fill(WritableHost to, Host from) {
    for (final var leaf : getLeafs()) {
      leaf.fill(to, from);
    }

    for (final var child : getChildren()) {
      fillChild(child, to, from);
    }
  }

  private static <ParentHost, Host, ChildHost extends Host> void fillChild(MetadataComposite<ParentHost, Host, ChildHost> metadata, ParentHost to, ParentHost from) {
    final var toChild = metadata.extractChildHost(to);
    final var fromChild = metadata.extractChildHost(from);

    metadata.fill(toChild, fromChild);

  } 

  @Override
  public void fill(WritableHost to, Host from, Tag tag) {
    for (final var leaf : getLeafs()) {
      leaf.fill(to, from, tag);
    }

    for (final var child : getChildren()) {
      fillChild(child, to, from, tag);
    }
  }

  private static <ParentHost, Host, ChildHost extends Host> void fillChild(MetadataComposite<ParentHost, Host, ChildHost> metadata, ParentHost to, ParentHost from, Tag tag) {
    final var toChild = metadata.extractChildHost(to);
    final var fromChild = metadata.extractChildHost(from);

    metadata.fill(toChild, fromChild, tag);

  } 

  public WritableHost extractChildHost(ParentHost parentHost) {
    return hostExtractor.apply(parentHost);
  }
}
