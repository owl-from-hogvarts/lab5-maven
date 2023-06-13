package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import net.whitehorizont.apps.organization_collection_manager.core.collection.CoordinatesDefinition.Coordinates;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.DoubleFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.EnumFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidatedFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.EnrichedNode;
import net.whitehorizont.apps.organization_collection_manager.lib.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.Node;

@NonNullByDefault
public class OrganisationElementDefinition implements ICollectionElement {
  // DONE: make iterator for fields
  static final String ELEMENT_TITLE = "Organisation";

  // !!! METADATA !!!
  private static final FieldMetadataExtended<String, ICollection<OrganisationElementDefinition>> NAME_METADATA = new FieldMetadataExtended.Metadata<String, ICollection<OrganisationElementDefinition>>()
          .setDisplayedName("name")
          .setRequired("Company name must be specified")
          .setValueBuilder(new StringFactory())
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() >= 1, "String should not be empty"))
          .build();

  private static final FieldMetadataExtended<UUID_ElementId, ICollection<OrganisationElementDefinition>> ID_METADATA = 
      new FieldMetadataExtended.Metadata<UUID_ElementId, ICollection<OrganisationElementDefinition>>()
          .setDisplayedName("ID")
          .setRequired("ID must be provided for collection element")
          .addValidator((value, collection) -> {
            // pray once more 🙏
            final var hasDuplicateIds = collection.getEvery$().filter((element) -> {
              return element.getID().getValue().equals(value);
            }).count().map(count -> count > 0).blockingGet();

            return new ValidationResult<Boolean>(!hasDuplicateIds, "Duplicate ID's found! ID should be unique!");
            
          })
          .build();

  private static final FieldMetadataExtended<OrganisationType, ICollection<OrganisationElementDefinition>> TYPE_METADATA =
      new FieldMetadataExtended.Metadata<OrganisationType, ICollection<OrganisationElementDefinition>>()
      .setDisplayedName("type")
      .setRequired("Type of organisation should be specified!")
      .setHint(OrganisationType.getHint())
      .build();

  // private static final FieldMetadataExtended<Double, ICollection< ANNUAL_TURNOVER_METADATA =
  //  new FieldMetadataExtended.Metadata<Double, ICollection<()
  //  .setDisplayedName("Annual Turnover")
  //  .setRequired("Annual Turnover must be provided")
  //  .addValidator((value, _unused) -> new ValidationResult<>(value > 0.0, "Annual Turnover should be strictly above zero"))
  //  .build();

  public static FieldMetadataExtended<String, ICollection<OrganisationElementDefinition>> getNameMetadata() {
    return NAME_METADATA;
  }

  private static final EnrichedNode<FieldWithMetadata, List<Validator>> OrganisationElementMetadataTree = new EnrichedNode<>(ELEMENT_TITLE,
   new Pair[]{new Pair<>(NAME_METADATA,  NAME_METADATA.getValidators()), new Pair<>(ID_METADATA, ID_METADATA, ID_METADATA.getValidators() )},
   new Node[]{CoordinatesDefinition.COORDINATES_METADATA}
  );
  
  

  // !!! FIELDS !!!
  private final ValidatedFieldDefinition<String, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> name;
  private final ValidatedFieldDefinition<UUID_ElementId, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> ID;
  private final CoordinatesDefinition coordinates;
  private final ValidatedFieldDefinition<OrganisationType, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> type;
  private final ValidatedFieldDefinition<Double, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> annualTurnover;

  // !!! GETTERS !!!
  public ValidatedFieldDefinition<String, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> getName() {
    return name;
  }

  public ValidatedFieldDefinition<UUID_ElementId, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> getID() {
    return ID;
  }

  public ValidatedFieldDefinition<OrganisationType, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> getType() {
    return type;
  }

  public ValidatedFieldDefinition<Double, ICollection<OrganisationElementPrototype, OrganisationElementDefinition>> getAnnualTurnover() {
    return annualTurnover;
  }

  // !!! CONSTRUCTOR !!!
  public OrganisationElementDefinition(ICollection<OrganisationElementPrototype, OrganisationElementDefinition> collection,
      OrganisationElementPrototype prototype)
      throws ValidationError {
    this.name = new ValidatedFieldDefinition<String, ICollection<OrganisationElementDefinition.OrganisationElementPrototype, OrganisationElementDefinition>>(
        OrganisationElementDefinition.NAME_METADATA,
        prototype.name.getValue(),
        collection);

    this.ID = new ValidatedFieldDefinition<UUID_ElementId, ICollection<OrganisationElementDefinition.OrganisationElementPrototype, OrganisationElementDefinition>>(
        ID_METADATA, prototype.ID.getValue(),
        collection);

    this.annualTurnover = new ValidatedFieldDefinition<>(ANNUAL_TURNOVER_METADATA, prototype.annualTurnover.getValue(), collection);

    this.coordinates = new CoordinatesDefinition(prototype.coordinates);

    this.type = new ValidatedFieldDefinition<OrganisationType,ICollection<OrganisationElementPrototype,OrganisationElementDefinition>>(TYPE_METADATA, prototype.type.getValue(), collection);
  }

  public static class OrganisationElementRawData {
    private String name;
    private UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable
    private Coordinates coordinates;
    private OrganisationType type;
    private Double annualTurnover;

    private OrganisationElementRawData name(String name) {
      this.name = name;
      return this;
    }

    private OrganisationElementRawData annualTurnover(Double annualTurnover) {
      this.annualTurnover = annualTurnover;
      return this;
    }

    private OrganisationElementRawData ID(UUID_ElementId id) {
      this.ID = id;
      return this;
    }

    private OrganisationElementRawData coordinates(Coordinates coordinates) {
      this.coordinates = coordinates;
      return this;
    }

    private OrganisationElementRawData type(OrganisationType type) {
      this.type = type;
      return this;
    }
  }

  @Override
  public TitledNode<ReadonlyField<?>> getTree() {
    final List<ReadonlyField<?>> fieldDefinitions = new ArrayList<>();
    fieldDefinitions.add(getName());
    fieldDefinitions.add(getID());
    fieldDefinitions.add(getType());
    fieldDefinitions.add(getAnnualTurnover());

    final List<TitledNode<ReadonlyField<?>>> children = new ArrayList<>();
    children.add(coordinates.getTree());
    
    final var node = new TitledNode<>(ELEMENT_TITLE, fieldDefinitions, children);

    // totally safe
    return node;
  }

  @Override
  public String getDisplayedName() {
    return ELEMENT_TITLE;
  }

  @Override
  public int compareTo(ICollectionElement<OrganisationElementPrototype> other) {
    // will not return null, 'cause field is not nullable
    return (int) (this.annualTurnover.getValue() - other.getPrototype().annualTurnover.getValue());
  }
}
