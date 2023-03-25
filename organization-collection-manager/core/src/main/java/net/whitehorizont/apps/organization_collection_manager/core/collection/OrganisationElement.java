package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationResult;

public class OrganisationElement {
  private static final FieldMetadata<String, Collection<OrganisationElement>> NAME_METADATA = new FieldMetadata<String, Collection<OrganisationElement>>(new FieldMetadata.Metadata<String, Collection<OrganisationElement>>().setNullable(false).addValidator((value, _unused) -> new ValidationResult<>(value.length() < 1, "")));

  public static FieldMetadata<String, Collection<OrganisationElement>> getNameMetadata() {
    return NAME_METADATA;
  }

  private final FieldDefinition<String, Collection<OrganisationElement>> name;

  public FieldDefinition<String, Collection<OrganisationElement>> getName() {
    return name;
  }

  public OrganisationElement(Collection<OrganisationElement> collection, String name) throws ValidationError {
    this.name = new FieldDefinition<String, Collection<OrganisationElement>>(OrganisationElement.NAME_METADATA, name,
        collection);
  }
}
