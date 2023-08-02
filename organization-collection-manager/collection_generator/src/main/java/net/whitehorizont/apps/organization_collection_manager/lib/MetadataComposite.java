package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;


/** 
 * @param <ParentHost> parent context object
 * @param <Host> current context object
 * @param <WritableHost> 
 * @param <T> Validation object
 */
@NonNullByDefault
// we know type of node container i.e. TitledNode but now what is inside of it
public class MetadataComposite<ParentHost, Host, WritableHost extends Host, T > extends TitledNode<MetadataComposite<Host, ?, ?, ? super T>, FieldMetadataExtended<Host, WritableHost, ?>> implements IElementInfoProvider<Host, T>, ICanFill<Host, WritableHost> {
  private final Function<ParentHost, WritableHost> hostExtractor;

  public MetadataComposite(String displayedName, List<FieldMetadataExtended<Host, WritableHost, ?>> leafs,
      List<MetadataComposite<Host, ?, ?, ? super T>> children, Function<ParentHost, WritableHost> hostExtractor) {
    super(displayedName, leafs, children);
    this.hostExtractor = hostExtractor;
  }

  @Override
  public void validate(Host host, T validationObject) throws ValidationError {
    for (final var leaf : getLeafs()) {
      leaf.validate(host);
      
      // tried to overcome this crutch by using visitor pattern
      // turns out it is impossible to apply visitor pattern here
      // because there is a need to unwrap context object in type safe way
      // which is too hard or even impossible in java
      if (leaf instanceof FieldMetadataExtendedWithRichValidators) {
        ((FieldMetadataExtendedWithRichValidators<Host, WritableHost, ?, T>) leaf).validate(host, validationObject);
      }
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
      fillChild(child, to, from);
    }
  }

  private static <ParentHost, Host, ChildHost extends Host> void fillChild(MetadataComposite<ParentHost, Host, ChildHost, ?> metadata, ParentHost to, ParentHost from) {
    final var toChild = metadata.extractChildHost(to);
    final var fromChild = metadata.extractChildHost(from);

    metadata.fill(toChild, fromChild);

  } 

  public void fill(WritableHost to, Host from, Tag tag) {
    for (final var leaf : getLeafs()) {
      leaf.fill(to, from, tag);
    }

    for (final var child : getChildren()) {
      fillChild(child, to, from, tag);
    }
  }

  private static <ParentHost, Host, ChildHost extends Host> void fillChild(MetadataComposite<ParentHost, Host, ChildHost, ?> metadata, ParentHost to, ParentHost from, Tag tag) {
    final var toChild = metadata.extractChildHost(to);
    final var fromChild = metadata.extractChildHost(from);

    metadata.fill(toChild, fromChild, tag);

  } 

  public WritableHost extractChildHost(ParentHost parentHost) {
    return hostExtractor.apply(parentHost);
  }
}
