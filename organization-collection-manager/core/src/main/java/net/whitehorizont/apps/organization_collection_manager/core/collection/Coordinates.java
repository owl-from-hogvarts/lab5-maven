package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataWithValidators;
import net.whitehorizont.apps.organization_collection_manager.lib.IntegerFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;

@NonNullByDefault
public class Coordinates implements IWithPrototype<Coordinates.CoordinatesPrototype> {
  private static final String COORDINATES_TITLE = "Coordinates";
  
  private static final FieldMetadataWithValidators<Integer, Object> X_METADATA = new FieldMetadataWithValidators.Metadata<Integer, Object>().addValidator((value, _unused) -> {
    final int x = value.intValue();
    final var isValueOk = x > -802;
    final var result = new ValidationResult<>(isValueOk, "Value should be strictly above -802");
    return result;
  }).setDisplayedName("X")
  .build();

  private final FieldDefinition<Integer, ?> x;

  public Coordinates(CoordinatesPrototype prototype) throws ValidationError {
    this.x = new FieldDefinition<Integer, Object>(X_METADATA, prototype.x.getValue(), new Object());
  }

  public static class CoordinatesRawData {
    private int x = 0;

    public CoordinatesRawData setX(int x) {
      this.x = x;
      return this;
    }
  }

  public static class CoordinatesPrototype implements IElementPrototype<Coordinates.CoordinatesRawData> {
    private final WritableFromStringFieldDefinition<Integer> x;

    public CoordinatesPrototype() {
      try {
        this.x = new WritableFromStringFieldDefinition<Integer>(X_METADATA, 0, new IntegerFactory());
      } catch (ValidationError e) {
        assert false;
        throw new RuntimeException();
      }
    }

    @Override
    public CoordinatesRawData getRawElementData() {
      return new CoordinatesRawData().setX(this.x.getValue());
    }

    @Override
    public IElementPrototype<CoordinatesRawData> setFromRawData(CoordinatesRawData rawData) throws ValidationError {
      this.x.setValue(rawData.x);
      return this;
    }

    @Override
    public Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields() {
      final List<WritableFromStringFieldDefinition<?>> fields = new ArrayList<>();
      fields.add(x);

      return fields;
    }

    @Override
    public Iterable<IWriteableFieldDefinitionNode> getChildren() {
      return new ArrayList<>();
    }

    @Override
    public String getDisplayedName() {
      return COORDINATES_TITLE;
    }
  }

  @Override
  public String getDisplayedName() {
    return COORDINATES_TITLE;
  }

  @Override
  public CoordinatesPrototype getPrototype() {
    final var prototype = new CoordinatesPrototype();
    try {
      prototype.x.setValue(this.x.getValue());

      return prototype;
    } catch (ValidationError e) {
      // valid element is valid prototype
      // if validation error happens -> error in program
      assert false;
      throw new RuntimeException();
    }
  }

  @Override
  public TitledNode<ReadonlyField<?>> getTree() {
    final List<ReadonlyField<?>> leafs = new ArrayList<>();
    leafs.add(new ReadonlyField<>(X_METADATA, x.getValue()));

    return new TitledNode<>(COORDINATES_TITLE, leafs, new ArrayList<>());
  }
}
