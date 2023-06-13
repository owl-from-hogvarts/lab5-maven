package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CoordinatesDefinition.Coordinates;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;

@NonNullByDefault
public class OrganisationElementDefinition {
  // DONE: make iterator for fields
  static final String ELEMENT_TITLE = "Organisation";

  // !!! METADATA !!!
  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, String, ICollection<OrganisationElement>> NAME_METADATA = new FieldMetadataExtended.Metadata<OrganisationElement, OrganisationElementWritable, String, ICollection<OrganisationElement>>()
          .setDisplayedName("name")
          .setRequired("Company name must be specified")
          .setValueBuilder(new StringFactory())
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() >= 1, "String should not be empty"))
          .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, UUID_ElementId, ICollection<OrganisationElement>> ID_METADATA = 
      new FieldMetadataExtended.Metadata<OrganisationElement, OrganisationElementWritable, UUID_ElementId, ICollection<OrganisationElement>>()
          .setDisplayedName("ID")
          .setRequired("ID must be provided for collection element")
          .addValidator((value, collection) -> {
            // pray once more 🙏
            final var hasDuplicateIds = collection.getEvery$().filter((element) -> {
              return element.ID.equals(value);
            }).count().map(count -> count > 0).blockingGet();

            return new ValidationResult<Boolean>(!hasDuplicateIds, "Duplicate ID's found! ID should be unique!");
            
          })
          .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, OrganisationType, ICollection<OrganisationElement>> TYPE_METADATA =
      new FieldMetadataExtended.Metadata<OrganisationElement, OrganisationElementWritable, OrganisationType, ICollection<OrganisationElement>>()
      .setDisplayedName("type")
      .setRequired("Type of organisation should be specified!")
      .setHint(OrganisationType.getHint())
      .build();

  public static final FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, Double, ICollection<OrganisationElement>> ANNUAL_TURNOVER_METADATA =
   new FieldMetadataExtended.Metadata<OrganisationElement, OrganisationElementWritable, Double, ICollection<OrganisationElement>>()
   .setDisplayedName("Annual Turnover")
   .setRequired("Annual Turnover must be provided")
   .addValidator((value, _unused) -> new ValidationResult<>(value > 0.0, "Annual Turnover should be strictly above zero"))
   .build();

  public static MetadataComposite<OrganisationElement, OrganisationElementWritable, ?> getMetadata() {
    final List<FieldMetadataExtended<OrganisationElement, OrganisationElementWritable, ?, ?>> leafs = new ArrayList<>();
    leafs.add(ID_METADATA);
    leafs.add(NAME_METADATA);
    leafs.add(TYPE_METADATA);
    leafs.add(ANNUAL_TURNOVER_METADATA);

    final var children = new ArrayList<>();
    children.add(CoordinatesDefinition.getTree());

    return new MetadataComposite<>(ELEMENT_TITLE, leafs, children);
  }
  
  public static class OrganisationElement implements ICollectionElement<OrganisationElement> {
    protected String name;
    protected UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable
    protected Coordinates coordinates;
    protected OrganisationType type;
    protected Double annualTurnover;
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

    private OrganisationElementWritable coordinates(Coordinates coordinates) {
      this.coordinates = coordinates;
      return this;
    }

    private OrganisationElementWritable type(OrganisationType type) {
      this.type = type;
      return this;
    }
  }

}
