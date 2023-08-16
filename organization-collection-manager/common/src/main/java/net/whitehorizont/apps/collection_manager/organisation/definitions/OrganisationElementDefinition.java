package net.whitehorizont.apps.collection_manager.organisation.definitions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.organisation.definitions.AddressDefinition.AddressWritable;
import net.whitehorizont.apps.collection_manager.organisation.definitions.CoordinatesDefinition.Coordinates;
import net.whitehorizont.apps.collection_manager.organisation.definitions.CoordinatesDefinition.CoordinatesWriteable;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.DoubleFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.EnumFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;

@NonNullByDefault
public class OrganisationElementDefinition {
  // DONE: make iterator for fields
  static final String ELEMENT_TITLE = "Organisation";

  // !!! METADATA !!!
  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, String> NAME_METADATA = FieldMetadataExtended
      .<OrganisationElement, OrganisationElementWritable, String>builder()
      .setDisplayedName("name")
      .setRequired("Company name must be specified")
      .setValueBuilder(new StringFactory())
      .setValueSetter((element, name) -> element.name(name))
      .setValueGetter(element -> element.getName())
      .addSimpleValidator((value) -> new ValidationResult<>(value.length() >= 1, "String should not be empty"))
      .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, UUID_ElementId> ID_METADATA = FieldMetadataExtended
      .<OrganisationElement, OrganisationElementWritable, UUID_ElementId>builder()
      .setDisplayedName("ID")
      .setRequired("ID must be provided for collection element")
      .setValueSetter((element, ID) -> element.ID(ID))
      .setValueGetter(element -> element.getID())
      .addTag(Tag.PRESERVE)
      .addTag(Tag.SKIP_INTERACTIVE_INPUT)
      .setValueBuilder((idString) -> new UUID_ElementId(idString))
      .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, OrganisationType> TYPE_METADATA = FieldMetadataExtended
      .<OrganisationElement, OrganisationElementWritable, OrganisationType>builder()
      .setDisplayedName("type")
      .setValueBuilder(new EnumFactory<>(OrganisationType.class))
      .setRequired("Type of organisation should be specified!")
      .setHint(OrganisationType.getHint())
      .setValueGetter(element -> element.getType())
      .setValueSetter((element, type) -> element.type(type))
      .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, Double> ANNUAL_TURNOVER_METADATA = FieldMetadataExtended
      .<OrganisationElement, OrganisationElementWritable, Double>builder()
      .setDisplayedName("Annual Turnover")
      .setRequired("Annual Turnover must be provided")
      .addSimpleValidator(value -> new ValidationResult<>(value > 0.0, "Annual Turnover should be strictly above zero"))
      .setValueGetter(element -> element.getAnnualTurnover())
      .setValueSetter((element, annualTurnover) -> element.annualTurnover(annualTurnover))
      .setValueBuilder(new DoubleFactory())
      .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, String> FULL_NAME_METADATA = FieldMetadataExtended
      .<OrganisationElement, OrganisationElementWritable, String>builder()
      .setDisplayedName("Full name")
      .setRequired("Full name can't be empty")
      .addSimpleValidator(
          fullName -> new ValidationResult<>(fullName.length() > 0, "Full name must contain at least one symbol"))
      .setValueBuilder(new StringFactory())
      .setValueGetter(host -> host.getFullName())
      .setValueSetter((host, value) -> host.fullName(value))
      .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, LocalDateTime> CREATION_DATE_METADATA = FieldMetadataExtended
      .<OrganisationElement, OrganisationElementWritable, LocalDateTime>builder()
      .setDisplayedName("Creation date")
      .setRequired(null)
      .addTag(Tag.SKIP_INTERACTIVE_INPUT)
      .addTag(Tag.PRESERVE)
      .setValueGetter(host -> host.getCreationDate())
      .setValueSetter((element, value) -> element.creationDate(value))
      .build();

  public static MetadataComposite<?, OrganisationElement, OrganisationElementWritable> getMetadata() {
    final List<FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, ?>> leafs = new ArrayList<>();
    leafs.add(ID_METADATA);
    leafs.add(NAME_METADATA);
    leafs.add(TYPE_METADATA);
    leafs.add(ANNUAL_TURNOVER_METADATA);
    leafs.add(FULL_NAME_METADATA);
    leafs.add(CREATION_DATE_METADATA);

    final List<MetadataComposite<OrganisationElement, ?, ?>> children = new ArrayList<>();
    children.add(CoordinatesDefinition.<OrganisationElement>getTree((organisation) -> organisation.coordinates));
    children.add(AddressDefinition.getMetadata(organisation -> organisation.address));

    return new MetadataComposite<Object, OrganisationElement, OrganisationElementWritable>(ELEMENT_TITLE, leafs,
        children, null);
  }

  public static class OrganisationElementFactory implements IWritableHostFactory<OrganisationElementWritable> {

    @Override
    public OrganisationElementWritable createWritable() {
      return new OrganisationElementWritable();
    }

  }

  public static class OrganisationElement implements ICollectionElement<OrganisationElement> {
    protected String name;
    protected UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable
    protected CoordinatesWriteable coordinates = new CoordinatesWriteable();
    protected OrganisationType type;
    protected Double annualTurnover;
    protected AddressWritable address = new AddressWritable();
    protected String fullName;
    protected LocalDateTime creationDate = LocalDateTime.now();

    public LocalDateTime getCreationDate() {
      return creationDate;
    }

    public String getFullName() {
      return fullName;
    }

    protected String getName() {
      return name;
    }

    protected UUID_ElementId getID() {
      return ID;
    }

    protected Coordinates getCoordinates() {
      return coordinates;
    }

    protected OrganisationType getType() {
      return type;
    }

    protected Double getAnnualTurnover() {
      return annualTurnover;
    }

    @Override
    public int compareTo(OrganisationElement other) {
      // will not return null, 'cause field is not nullable
      return (int) (this.annualTurnover - other.annualTurnover);
    }
  }

  public static class OrganisationElementWritable extends OrganisationElement {

    private OrganisationElementWritable creationDate(LocalDateTime creationDate) {
      this.creationDate = creationDate;
      return this;
    }
    

    private OrganisationElementWritable fullName(String fullName) {
      this.fullName = fullName;
      return this;
    }

    private OrganisationElementWritable name(String name) {
      this.name = name;
      return this;
    }

    private OrganisationElementWritable annualTurnover(Double annualTurnover) {
      this.annualTurnover = annualTurnover;
      return this;
    }

    private OrganisationElementWritable ID(UUID_ElementId id) {
      this.ID = id;
      return this;
    }

    private OrganisationElementWritable type(OrganisationType type) {
      this.type = type;
      return this;
    }
  }

}