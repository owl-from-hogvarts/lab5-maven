package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationResult;

@NonNullByDefault
public class OrganisationElement implements ICollectionElement<OrganisationElement.Builder> {
  // TODO: make itertor for fields
  private static final FieldMetadata<String, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> NAME_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<String, ICollection<OrganisationElement.Builder, OrganisationElement, ?>>()
          .setNullable(false, "Company name must be specified")
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() >= 1, "")));

  private static final FieldMetadata<UUID_ElementId, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> ID_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<UUID_ElementId, ICollection<OrganisationElement.Builder, OrganisationElement, ?>>().setNullable(false,
          "ID must be provided for collection element"));

  public static FieldMetadata<String, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> getNameMetadata() {
    return NAME_METADATA;
  }

  private final FieldDefinition<String, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> name;
  private final FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> ID;

  public FieldDefinition<String, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> getName() {
    return name;
  }

  public FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.Builder, OrganisationElement, ?>> getID() {
    return ID;
  }

  public OrganisationElement(ICollection<Builder, OrganisationElement, ?> collection, Builder builder)
      throws ValidationError {
    this.name = new FieldDefinition<String, ICollection<OrganisationElement.Builder, OrganisationElement, ?>>(OrganisationElement.NAME_METADATA,
        builder.name,
        collection);

    this.ID = new FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.Builder, OrganisationElement, ?>>(ID_METADATA, builder.ID,
        collection);
  }

  public static class Builder implements IElementPrototype {
    private @Nullable String name;
    private UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder ID(UUID_ElementId id) {
      this.ID = id;
      return this;
    }
  }

  @Override
  public UUID_ElementId getId() {
    return this.ID.getValue();
  }

  @Override
  public Builder getPrototype() {
    return new Builder().ID(this.getID().getValue()).name(this.getName().getValue());
  }
}
