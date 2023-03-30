package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationResult;

public class OrganisationElement implements IWithId<ElementId> {
  private static final FieldMetadata<String, Collection<Builder, OrganisationElement>> NAME_METADATA = new FieldMetadata<String, Collection<Builder, OrganisationElement>>(
      new FieldMetadata.Metadata<String, Collection<Builder, OrganisationElement>>()
          .setNullable(false, "Company name must be specified")
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() < 1, "")));

  private static final FieldMetadata<ElementId, Collection<Builder, OrganisationElement>> ID_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<ElementId, Collection<Builder, OrganisationElement>>().setNullable(false,
          "ID must be provided for collection element"));

  public static FieldMetadata<String, Collection<Builder, OrganisationElement>> getNameMetadata() {
    return NAME_METADATA;
  }

  private final FieldDefinition<String, Collection<Builder, OrganisationElement>> name;
  private final FieldDefinition<ElementId, Collection<Builder, OrganisationElement>> ID;

  public FieldDefinition<String, Collection<Builder, OrganisationElement>> getName() {
    return name;
  }

  public OrganisationElement(Collection<Builder, OrganisationElement> collection, Builder builder)
      throws ValidationError {
    this.name = new FieldDefinition<String, Collection<Builder, OrganisationElement>>(OrganisationElement.NAME_METADATA,
        builder.name,
        collection);

    this.ID = new FieldDefinition<ElementId, Collection<Builder, OrganisationElement>>(ID_METADATA, builder.ID,
        collection);
  }

  public static class Builder {
    private String name;
    private ElementId ID;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder ID(ElementId id) {
      this.ID = id;
      return this;
    }
  }

  @Override
  public ElementId getId() {
    return this.ID.getValue();
  }
}
