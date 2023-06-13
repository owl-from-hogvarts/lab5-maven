package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;


@NonNullByDefault
public class MetadataComposite<Host, WritableHost extends Host, T> extends TitledNode<MetadataComposite<Host, WritableHost, T>, FieldMetadataExtended<Host, WritableHost, ?, T>> implements ICanValidate<Host, T> {

  public MetadataComposite(String displayedName, List<FieldMetadataExtended<Host, WritableHost, ?, T>> leafs,
      List<MetadataComposite<Host, WritableHost, T>> children) {
    super(displayedName, leafs, children);
  }

  @Override
  public void validate(Host host, T validationObject) throws ValidationError {
    for (final var leaf : getLeafs()) {
      leaf.validate(host, validationObject);
    }

    for (final var child : getChildren()) {
      child.validate(host, validationObject);
    }
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
      leaf.fill(to, from);
    }

    for (final var child : getChildren()) {
      child.fill(to, from);
    }
  }


}
