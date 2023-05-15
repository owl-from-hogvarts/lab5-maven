package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinition;

@NonNullByDefault
public class OrganisationElement implements ICollectionElement<OrganisationElement.OrganisationElementPrototype> {
  // TODO: make itertor for fields
  private static final FieldMetadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> NAME_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>()
          .setDisplayedName("name")
          .setNullable(false, "Company name must be specified")
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() >= 1, "")));

  private static final FieldMetadata<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> ID_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>()
          .setNullable(false,
              "ID must be provided for collection element"));

  public static FieldMetadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> getNameMetadata() {
    return NAME_METADATA;
  }

  private final FieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> name;
  private final FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> ID;

  public FieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> getName() {
    return name;
  }

  public FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> getID() {
    return ID;
  }

  public OrganisationElement(ICollection<OrganisationElementPrototype, OrganisationElement, ?> collection,
      OrganisationElementPrototype builder)
      throws ValidationError {
    this.name = new FieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>(
        OrganisationElement.NAME_METADATA,
        builder.name.getValue(),
        collection);

    this.ID = new FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>(
        ID_METADATA, builder.ID.getValue(),
        collection);
  }

  public static class OrganisationElementRawData {
    private @Nullable String name;
    private UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable

    private OrganisationElementRawData name(@Nullable String name) {
      this.name = name;
      return this;
    }

    public OrganisationElementRawData ID(UUID_ElementId id) {
      this.ID = id;
      return this;
    }
  }

  @Override
  public UUID_ElementId getId() {
    return this.ID.getValue();
  }

  @Override
  public OrganisationElementPrototype getPrototype() {
    final var prototype = new OrganisationElementPrototype();
    try {
      prototype.ID.setValue(this.ID.getValue());
      prototype.name.setValue(this.name.getValue());

      return prototype;
    } catch (ValidationError e) {
      // valid element is valid prototype
      // if validation error happens -> error in program
      assert false;
      throw new RuntimeException();
    }

  }

  public static class OrganisationElementPrototype
      implements IElementPrototype<OrganisationElement.OrganisationElementRawData> {
    private final WriteableFieldDefinition<UUID_ElementId> ID;
    private final WritableFromStringFieldDefinition<String> name;

    private final List<WritableFromStringFieldDefinition<?>> fields = new ArrayList<>();

    public OrganisationElementPrototype() {
      try {
        this.ID = new WriteableFieldDefinition<>(OrganisationElement.ID_METADATA, new UUID_ElementId());

        this.name = new WritableFromStringFieldDefinition<String>(NAME_METADATA, new StringFactory(), "");
        this.fields.add(name);

      } catch (ValidationError e) {
        assert false;
        throw new RuntimeException();
      }

    }

    @Override
    public OrganisationElementRawData getRawElementData() {
      return new OrganisationElementRawData().ID(this.ID.getValue()).name(this.name.getValue());
    }

    @Override
    public Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields() {
      return this.fields;
    }

    // @Override
    // public Iterable<WriteableFieldDefinition<?>> getDisplayableFields() {
    // return this.getDisplayableFields();
    // }

  }
}
