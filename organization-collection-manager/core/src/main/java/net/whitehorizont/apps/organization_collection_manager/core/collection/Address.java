package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataWithValidators;
import net.whitehorizont.apps.organization_collection_manager.lib.IWriteableFieldDefinitionNode;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.StringFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.apps.organization_collection_manager.lib.WritableFromStringFieldDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;


@NonNullByDefault
public class Address implements IWithPrototype<Address.AddressPrototype> {
  private static final String ADDRESS_TITLE = "Address";
  
  private static final FieldMetadataWithValidators<String, Object> STREET_METADATA= 
      new FieldMetadataWithValidators.Metadata<String, Object>()
      .addValidator((value, _unused) -> new ValidationResult(value.length() > 0, "string can not be empty!"))
      .setDisplayedName("Street")
      .setRequired("Street must be provided!")
      .build();
  
  private final FieldDefinition<String, Object> street;
  
  public Address(AddressPrototype prototype) throws ValidationError {
    this.street = new FieldDefinition<String,Object>(STREET_METADATA, prototype.street.getValue(), new Object());
  }

  public static class AddressPrototype implements IElementPrototype<AddressRawData> {
    private final WritableFromStringFieldDefinition<String> street;

    public AddressPrototype() {
      try {
        this.street = new WritableFromStringFieldDefinition<String>(STREET_METADATA, "=()0", new StringFactory());
      } catch (ValidationError _ignore) {
        // should never happen
        assert false;
        throw new RuntimeException();
      }
    }

    @Override
    public Iterable<WritableFromStringFieldDefinition<?>> getWriteableFromStringFields() {
      final List<WritableFromStringFieldDefinition<?>> fields = new ArrayList<>();
      fields.add(street);

      return fields;
    }

    @Override
    public Iterable<IWriteableFieldDefinitionNode> getChildren() {
      return new ArrayList<>();
    }
    

    @Override
    public String getDisplayedName() {
      return ADDRESS_TITLE;
    }

    @Override
    public AddressRawData getRawElementData() {
      final var rawData = new AddressRawData();
      rawData.street = this.street.getValue();
      return rawData;
    }

    @Override
    public IElementPrototype<AddressRawData> setFromRawData(AddressRawData rawData) throws ValidationError {
      this.street.setValue(rawData.street);
      return this;
    }

  }

  public static class AddressRawData {
    private String street;
    
    AddressRawData street(String street) {
      this.street = street;
      return this;
    }
  }

  @Override
  public TitledNode<ReadonlyField<?>> getTree() {
    final List<ReadonlyField<?>> leafs = new ArrayList<>();
    leafs.add(new ReadonlyField<>(STREET_METADATA, street.getValue()));

    return new TitledNode<>(ADDRESS_TITLE, leafs, new ArrayList<>());
  }

  @Override
  public String getDisplayedName() {
    return ADDRESS_TITLE;
  }

  @Override
  public AddressPrototype getPrototype() {
    final var prototype = new AddressPrototype();
    try {
      prototype.street.setValue(this.street.getValue());
    } catch (ValidationError e) {
      // valid element is valid prototype
      // if validation error happens -> error in program
      assert false;
      throw new RuntimeException();

    }

    return prototype;
  }
}
