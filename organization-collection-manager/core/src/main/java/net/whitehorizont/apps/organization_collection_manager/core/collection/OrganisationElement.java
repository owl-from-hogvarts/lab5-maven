package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.Coordinates.CoordinatesPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.Coordinates.CoordinatesRawData;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinitionNode;

@NonNullByDefault
public class OrganisationElement implements ICollectionElement<OrganisationElement.OrganisationElementPrototype> {
  // DONE: make iterator for fields
  private static final String ELEMENT_TITLE = "Organisation";

  private static final FieldMetadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> NAME_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>()
          .setDisplayedName("name")
          .setRequired("Company name must be specified")
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() >= 1, "String should not be empty")));

  private static final FieldMetadata<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> ID_METADATA = new FieldMetadata<>(
      new FieldMetadata.Metadata<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>()
          .setDisplayedName("ID")
          .setRequired("ID must be provided for collection element"));

  public static FieldMetadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> getNameMetadata() {
    return NAME_METADATA;
  }

  private final FieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> name;
  private final FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> ID;
  private final Coordinates coordinates;

  public FieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> getName() {
    return name;
  }

  public FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>> getID() {
    return ID;
  }

  public OrganisationElement(ICollection<OrganisationElementPrototype, OrganisationElement, ?> collection,
      OrganisationElementPrototype prototype)
      throws ValidationError {
    this.name = new FieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>(
        OrganisationElement.NAME_METADATA,
        prototype.name.getValue(),
        collection);

    this.ID = new FieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement, ?>>(
        ID_METADATA, prototype.ID.getValue(),
        collection);

    this.coordinates = new Coordinates(prototype.coordinates);
  }

  public static class OrganisationElementRawData {
    private String name = "-()0=";
    private UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable
    private CoordinatesRawData coordinates = new CoordinatesRawData();

    private OrganisationElementRawData name(String name) {
      this.name = name;
      return this;
    }

    public OrganisationElementRawData ID(UUID_ElementId id) {
      this.ID = id;
      return this;
    }

    private OrganisationElementRawData coordinates(CoordinatesRawData coordinates) {
      this.coordinates = coordinates;
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
      prototype.coordinates = this.coordinates.getPrototype();

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
    private CoordinatesPrototype coordinates = new CoordinatesPrototype();

    private final List<WritableFromStringFieldDefinition<?>> fields = new ArrayList<>();

    public OrganisationElementPrototype() {
      try {
        this.ID = new WriteableFieldDefinition<>(OrganisationElement.ID_METADATA, new UUID_ElementId());

        this.name = new WritableFromStringFieldDefinition<String>(NAME_METADATA, new StringFactory(), "");
        this.fields.add(name);

      } catch (ValidationError e) {
        assert false; // default value is hardcoded. if error happens here, it is a bug
        throw new RuntimeException();
      }

    }

    @Override
    public OrganisationElementRawData getRawElementData() {
      return new OrganisationElementRawData().ID(this.ID.getValue()).name(this.name.getValue())
          .coordinates(coordinates.getRawElementData());
    }

    @Override
    public Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields() {
      return this.fields;
    }

    @Override
    public IElementPrototype<OrganisationElementRawData> setFromRawData(OrganisationElementRawData rawData)
        throws ValidationError {

      this.ID.setValue(rawData.ID);
      this.name.setValueFromString(rawData.name);
      this.coordinates.setFromRawData(rawData.coordinates);

      return this;
    }

    @Override
    public Iterable<WriteableFieldDefinitionNode> getChildren() {
      final List<WriteableFieldDefinitionNode> children = new ArrayList<>();
      children.add(coordinates);

      return children;
    }

    @Override
    public String getDisplayedName() {
      return ELEMENT_TITLE;
    }
  }

  @Override
  public Iterable<FieldDefinition<?, ?>> getFields() {
    final List<FieldDefinition<?, ?>> fieldDefinitions = new ArrayList<>();
    fieldDefinitions.add(getName());
    fieldDefinitions.add(getID());

    return fieldDefinitions;
  }

  @Override
  public String getDisplayedName() {
    return ELEMENT_TITLE;
  }

  @Override
  public Iterable<IFieldDefinitionNode> getChildren() {
    final List<IFieldDefinitionNode> children = new ArrayList<>();
    children.add(coordinates);

    return children;
  }
}
