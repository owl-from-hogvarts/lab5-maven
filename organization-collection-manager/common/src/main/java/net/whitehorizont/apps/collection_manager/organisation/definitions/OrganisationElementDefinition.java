package net.whitehorizont.apps.collection_manager.organisation.definitions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

  public static final FieldMetadataExtended<OrganisationElementFull, OrganisationElementFull, UUID_ElementId> ID_METADATA = FieldMetadataExtended
      .<OrganisationElementFull, OrganisationElementFull, UUID_ElementId>builder()
      .setDisplayedName("ID")
      .setRequired("ID must be provided for collection element")
      .setValueGetter(element -> element.metadata.getID())
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

  public static final FieldMetadataExtended<OrganisationElementFull, OrganisationElementFull, LocalDateTime> CREATION_DATE_METADATA = FieldMetadataExtended
      .<OrganisationElementFull, OrganisationElementFull, LocalDateTime>builder()
      .setDisplayedName("Creation date")
      .setRequired(null)
      .addTag(Tag.SKIP_INTERACTIVE_INPUT)
      .addTag(Tag.PRESERVE)
      .setValueGetter(host -> host.metadata.getCreationDate())
      .build();

  public static MetadataComposite<?, OrganisationElement, OrganisationElementWritable> getInputMetadata() {
    return getOrganisationMetadata(null);
  }

  private static <ParentHost> MetadataComposite<ParentHost, OrganisationElement, OrganisationElementWritable> getOrganisationMetadata(Function<ParentHost, OrganisationElementWritable> organisationExtractor) {
    final List<FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, ?>> leafs = new ArrayList<>();
    leafs.add(NAME_METADATA);
    leafs.add(TYPE_METADATA);
    leafs.add(ANNUAL_TURNOVER_METADATA);
    leafs.add(FULL_NAME_METADATA);

    final List<MetadataComposite<OrganisationElement, ?, ?>> children = new ArrayList<>();
    children.add(CoordinatesDefinition.getTree((organisation) -> organisation.coordinates));
    children.add(AddressDefinition.getMetadata(organisation -> organisation.address));

    return new MetadataComposite<ParentHost, OrganisationElement, OrganisationElementWritable>(ELEMENT_TITLE, leafs,
        children, organisationExtractor, organisationExtractor != null);
  }

  public static MetadataComposite<?, OrganisationElementFull, OrganisationElementFull> getMetadata() {
    final List<FieldMetadataExtended<OrganisationElementFull, OrganisationElementFull, ?>> leafs = new ArrayList<>();
    leafs.add(ID_METADATA);
    leafs.add(CREATION_DATE_METADATA);

    final List<MetadataComposite<OrganisationElementFull, ?, ?>> children = new ArrayList<>();
    children.add(getOrganisationMetadata((parent) -> parent.element));

    return new MetadataComposite<>(ELEMENT_TITLE, leafs, children, null);
  }

  public static class OrganisationElementFactory implements IWritableHostFactory<OrganisationElementWritable> {

    @Override
    public OrganisationElementWritable createWritable() {
      return new OrganisationElementWritable();
    }

  }

  public static class OrganisationElementFull implements ICollectionElement<OrganisationElementFull> {
    private final OrganisationElementWritable element;
    public OrganisationElementWritable getElement() {
      return element;
    }


    private final OrganisationElementAutogenerated metadata;

    public OrganisationElementAutogenerated getMetadata() {
      return metadata;
    }


    public OrganisationElementFull(OrganisationElementAutogenerated metadata, OrganisationElementWritable element) {
      this.metadata = metadata;
      this.element = element;
    }


    @Override
    public int compareTo(OrganisationElementFull other) {
      return (int) (this.element.getAnnualTurnover() - other.element.getAnnualTurnover());
    }
  }

  public static class OrganisationElementFullWritable extends OrganisationElementFull {

    public OrganisationElementFullWritable(OrganisationElementAutogenerated metadata,
        OrganisationElementWritable element) {
      super(metadata, element);
    }



  }

  /**
   * ! THIS IS READONLY STRUCTURE !
   * 
   * DO NOT USE WITH "FILL" OPERATION
   */
  public static class OrganisationElementAutogenerated implements Serializable {
    protected UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable
    protected LocalDateTime creationDate = LocalDateTime.now();

    private LocalDateTime getCreationDate() {
      return creationDate;
    }

    private UUID_ElementId getID() {
      return ID;
    }
    
  }

  public static class OrganisationElement implements Serializable {
    protected String name;
    protected CoordinatesWriteable coordinates = new CoordinatesWriteable();
    protected OrganisationType type;
    protected Double annualTurnover;
    protected AddressWritable address = new AddressWritable();
    protected String fullName;

    public String getFullName() {
      return fullName;
    }

    protected String getName() {
      return name;
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
  }

  public static class OrganisationElementWritable extends OrganisationElement {
    

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

    private OrganisationElementWritable type(OrganisationType type) {
      this.type = type;
      return this;
    }
  }

}
