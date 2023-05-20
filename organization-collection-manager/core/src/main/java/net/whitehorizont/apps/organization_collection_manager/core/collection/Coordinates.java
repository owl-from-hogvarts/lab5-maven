package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.IFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.NumberFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationResult;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.WriteableFieldDefinitionNode;

@NonNullByDefault
public class Coordinates implements ICollectionElement<Coordinates.CoordinatesPrototype> {
  private static final String COORDINATES_TITLE = "Coordinates";
  
  private static final FieldMetadata<Integer, Object> X_METADATA = new FieldMetadata<>(new FieldMetadata.Metadata<Integer, Object>().addValidator((value, _unused) -> {
    final int x = value.intValue();
    final var isValueOk = x > -802;
    final var result = new ValidationResult<>(isValueOk, "Value should be strictly above -802");
    return result;
  }).setDisplayedName("X"));

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
        this.x = new WritableFromStringFieldDefinition<Integer>(X_METADATA, 0, new NumberFactory());
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
    public Iterable<WriteableFieldDefinitionNode> getChildren() {
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
  public Iterable<FieldDefinition<?, ?>> getFields() {
    final List<FieldDefinition<?, ?>> fields = new ArrayList<>();
    fields.add(x);

    return fields;
  }

  @Override
  public Iterable<IFieldDefinitionNode> getChildren() {
    return new ArrayList<>();
  }

  @Override
  public BaseId getId() {
    // TODO: refactor interfaces
    throw new UnsupportedOperationException("Unimplemented method 'getId'");
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
}
