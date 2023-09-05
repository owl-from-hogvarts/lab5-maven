package net.whitehorizont.apps.collection_manager.organisation.definitions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.organisation.definitions.LocationDefinition.Location;
import net.whitehorizont.apps.collection_manager.organisation.definitions.LocationDefinition.LocationWritable;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.StringFactory;

@NonNullByDefault
public class AddressDefinition {
  private static final String TITLE = "Address";

  public static FieldMetadataExtended<Address, AddressWritable, String> STREET_METADATA = FieldMetadataExtended
      .<Address, AddressWritable, String>builder()
      .setDisplayedName("Street")
      .setValueBuilder(new StringFactory())
      .setValueGetter(host -> host.street)
      .setValueSetter((host, value) -> host.setStreet(value))
      .setRequired(null)
      .build();
  public static FieldMetadataExtended<Address, AddressWritable, String> ZIP_CODE_METADATA = FieldMetadataExtended
      .<Address, AddressWritable, String>builder()
      .setDisplayedName("Zip code")
      .setValueSetter((host, value) -> host.setZipCode(value))
      .setValueGetter(host -> host.zipCode)
      .setValueBuilder(new StringFactory())
      .setRequired(null)
      .build();

  public static <ParentHost> MetadataComposite<ParentHost, Address, AddressWritable> getMetadata(
      Function<ParentHost, AddressWritable> addressExtractor) {
    final List<FieldMetadataExtended<Address, AddressWritable, ?>> leafs = new ArrayList<>();
    leafs.add(STREET_METADATA);
    leafs.add(ZIP_CODE_METADATA);

    final List<MetadataComposite<Address, ?, ?>> child = new ArrayList<>();
    child.add(LocationDefinition.getMetadata(address -> address.town));

    return new MetadataComposite<>(TITLE, leafs, child, addressExtractor);
  }

  public static class Address implements Serializable {
    protected String street;
    protected String zipCode;
    protected LocationWritable town = new LocationWritable();

    protected Location getTown() {
      return this.town;
    }
  }

  public static class AddressWritable extends Address {

    public void setStreet(String street) {
      this.street = street;
    }

    public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
    }

  }
}
