package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import net.whitehorizont.apps.organization_collection_manager.core.collection.CoordinatesDefinition.CoordinatesPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CoordinatesDefinition.CoordinatesRawData;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.DoubleFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.EnumFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidatedFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataWithValidators;
import net.whitehorizont.apps.organization_collection_manager.lib.EnrichedNode;
import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.Validator;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.Node;

@NonNullByDefault
public class OrganisationElement implements ICollectionElement<OrganisationElement.OrganisationElementPrototype> {
  // DONE: make iterator for fields
  static final String ELEMENT_TITLE = "Organisation";

  // !!! METADATA !!!
  private static final FieldMetadataWithValidators<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>> NAME_METADATA = new FieldMetadataWithValidators.Metadata<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>>()
          .setDisplayedName("name")
          .setRequired("Company name must be specified")
          .setValueBuilder(new StringFactory())
          .addValidator((value, _unused) -> new ValidationResult<>(value.length() >= 1, "String should not be empty"))
          .build();

  private static final FieldMetadataWithValidators<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>> ID_METADATA = 
      new FieldMetadataWithValidators.Metadata<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>>()
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

  private static final FieldMetadataWithValidators<OrganisationType, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>> TYPE_METADATA =
      new FieldMetadataWithValidators.Metadata<OrganisationType, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>>()
      .setDisplayedName("type")
      .setRequired("Type of organisation should be specified!")
      .setHint(OrganisationType.getHint())
      .build();

  private static final FieldMetadataWithValidators<Double, ICollection<OrganisationElementPrototype, OrganisationElement>> ANNUAL_TURNOVER_METADATA =
   new FieldMetadataWithValidators.Metadata<Double, ICollection<OrganisationElementPrototype, OrganisationElement>>()
   .setDisplayedName("Annual Turnover")
   .setRequired("Annual Turnover must be provided")
   .addValidator((value, _unused) -> new ValidationResult<>(value > 0.0, "Annual Turnover should be strictly above zero"))
   .build();

  public static FieldMetadataWithValidators<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>> getNameMetadata() {
    return NAME_METADATA;
  }

  private static final EnrichedNode<FieldWithMetadata, List<Validator>> OrganisationElementMetadataTree = new EnrichedNode<>(ELEMENT_TITLE,
   new Pair[]{new Pair<>(NAME_METADATA,  NAME_METADATA.getValidators()), new Pair<>(ID_METADATA, ID_METADATA, ID_METADATA.getValidators() )},
   new Node[]{CoordinatesDefinition.COORDINATES_METADATA}
  );
  
  

  // !!! FIELDS !!!
  private final ValidatedFieldDefinition<String, ICollection<OrganisationElementPrototype, OrganisationElement>> name;
  private final ValidatedFieldDefinition<UUID_ElementId, ICollection<OrganisationElementPrototype, OrganisationElement>> ID;
  private final CoordinatesDefinition coordinates;
  private final ValidatedFieldDefinition<OrganisationType, ICollection<OrganisationElementPrototype, OrganisationElement>> type;
  private final ValidatedFieldDefinition<Double, ICollection<OrganisationElementPrototype, OrganisationElement>> annualTurnover;

  // !!! GETTERS !!!
  public ValidatedFieldDefinition<String, ICollection<OrganisationElementPrototype, OrganisationElement>> getName() {
    return name;
  }

  public ValidatedFieldDefinition<UUID_ElementId, ICollection<OrganisationElementPrototype, OrganisationElement>> getID() {
    return ID;
  }

  public ValidatedFieldDefinition<OrganisationType, ICollection<OrganisationElementPrototype, OrganisationElement>> getType() {
    return type;
  }

  public ValidatedFieldDefinition<Double, ICollection<OrganisationElementPrototype, OrganisationElement>> getAnnualTurnover() {
    return annualTurnover;
  }

  // !!! CONSTRUCTOR !!!
  public OrganisationElement(ICollection<OrganisationElementPrototype, OrganisationElement> collection,
      OrganisationElementPrototype prototype)
      throws ValidationError {
    this.name = new ValidatedFieldDefinition<String, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>>(
        OrganisationElement.NAME_METADATA,
        prototype.name.getValue(),
        collection);

    this.ID = new ValidatedFieldDefinition<UUID_ElementId, ICollection<OrganisationElement.OrganisationElementPrototype, OrganisationElement>>(
        ID_METADATA, prototype.ID.getValue(),
        collection);

    this.annualTurnover = new ValidatedFieldDefinition<>(ANNUAL_TURNOVER_METADATA, prototype.annualTurnover.getValue(), collection);

    this.coordinates = new CoordinatesDefinition(prototype.coordinates);

    this.type = new ValidatedFieldDefinition<OrganisationType,ICollection<OrganisationElementPrototype,OrganisationElement>>(TYPE_METADATA, prototype.type.getValue(), collection);
  }

  // is itself only a data structure
  // should be replaced with tree
  // ! raw data is replaced with depleted tree (contains only values)
  // new Node<RawField>({RAW_NAME, RAW_ID}, {RAW_COORDINATES})
  public static class OrganisationElementRawData {
    private String name = "-()0=";
    private UUID_ElementId ID = new UUID_ElementId(); // init with default value which is easily overridable
    private CoordinatesRawData coordinates = new CoordinatesRawData();
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

    public OrganisationElementRawData ID(UUID_ElementId id) {
      this.ID = id;
      return this;
    }

    private OrganisationElementRawData coordinates(CoordinatesRawData coordinates) {
      this.coordinates = coordinates;
      return this;
    }

    private OrganisationElementRawData type(OrganisationType type) {
      this.type = type;
      return this;
    }
  }



  // !!! BUILD PROTOTYPE !!!
  @Override
  public OrganisationElementPrototype getPrototype() {
    final var prototype = new OrganisationElementPrototype();
    try {
      prototype.ID.setValue(this.ID.getValue());
      prototype.name.setValue(this.name.getValue());
      prototype.coordinates = this.coordinates.getPrototype();
      prototype.type.setValue(this.type.getValue());
      prototype.annualTurnover.setValue(this.annualTurnover.getValue());

      return prototype;
    } catch (ValidationError e) {
      // valid element is valid prototype
      // if validation error happens -> error in program
      assert false;
      throw new RuntimeException();
    }

  }

  // redundant. generated tree is checked against metadata
  public static class OrganisationElementPrototype
      implements IElementPrototype<OrganisationElement.OrganisationElementRawData> {

    private final WriteableFieldDefinition<UUID_ElementId> ID;
    private final WritableFromStringFieldDefinition<String> name;
    private CoordinatesPrototype coordinates = new CoordinatesPrototype();
    private final WritableFromStringFieldDefinition<OrganisationType> type;
    private final WritableFromStringFieldDefinition<Double> annualTurnover;

    private final List<WritableFromStringFieldDefinition<?>> inputFields = new ArrayList<>();

    public OrganisationElementPrototype() {
      try {
        this.ID = new WriteableFieldDefinition<>(OrganisationElement.ID_METADATA, new UUID_ElementId());

        this.name = new WritableFromStringFieldDefinition<String>(NAME_METADATA, new StringFactory(), "");
        this.inputFields.add(name);

        this.type = new WritableFromStringFieldDefinition<OrganisationType>(TYPE_METADATA, OrganisationType.COMMERCIAL ,new EnumFactory<>(OrganisationType.class));
        this.inputFields.add(type);

        this.annualTurnover = new WritableFromStringFieldDefinition<Double>(ANNUAL_TURNOVER_METADATA, 1.0, new DoubleFactory());
        this.inputFields.add(annualTurnover);

      } catch (ValidationError e) {
        assert false; // default value is hardcoded. if error happens here, it is a bug
        throw new RuntimeException();
      }

    }

    // !!! GET RAW ELEMENT DATA !!!
    @Override
    public OrganisationElementRawData getRawElementData() {
      return new OrganisationElementRawData()
        .ID(this.ID.getValue())
        .name(this.name.getValue())
        .coordinates(coordinates.getRawElementData())
        .type(this.type.getValue())
        .annualTurnover(this.annualTurnover.getValue());
    }

    @Override
    public List<WritableFromStringFieldDefinition<?>> getLeafs() {
      return this.inputFields;
    }

    // !!! SET FROM RAW DATA !!!
    @Override
    public IElementPrototype<OrganisationElementRawData> setFromRawData(OrganisationElementRawData rawData)
        throws ValidationError {

      this.ID.setValue(rawData.ID);
      this.name.setValueFromString(rawData.name);
      this.coordinates.setFromRawData(rawData.coordinates);
      this.type.setValue(rawData.type);
      this.annualTurnover.setValue(rawData.annualTurnover);

      return this;
    }

    @Override
    public List<Node<WritableFromStringFieldDefinition<?>>> getChildren() {
      final List<IWriteableFieldDefinitionNode> children = new ArrayList<>();
      children.add(coordinates);

      return children;
    }

    @Override
    public String getDisplayedName() {
      return ELEMENT_TITLE;
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
