package net.whitehorizont.apps.collection_manager.organisation.definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.IntegerFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.StringFactory;

@NonNullByDefault
public class LocationDefinition {
  private static final String TITLE = "Location";
  
  public static FieldMetadataExtended<Location, LocationWritable, Float> X_METADATA = FieldMetadataExtended
      .<Location, LocationWritable, Float>builder()
      // just use default on null message
      .setRequired(null)
      .setValueGetter(host -> host.x)
      .setValueSetter((host, value) -> host.setX(value))
      .setValueBuilder(new IntegerFactory<>(Float.class))
      .setDisplayedName("X")
      .build();
  public static FieldMetadataExtended<Location, LocationWritable, Double> Y_METADATA = FieldMetadataExtended
      .<Location, LocationWritable, Double>builder()
      .setRequired(null)
      .setValueGetter(host -> host.y)
      .setValueSetter((host, value) -> host.setY(value))
      .setValueBuilder(new IntegerFactory<>(Double.class))
      .setDisplayedName("Y")
      .build();
  public static FieldMetadataExtended<Location, LocationWritable, Integer> Z_METADATA = FieldMetadataExtended
      .<Location, LocationWritable, Integer>builder()
      .setDisplayedName("Z")
      .setValueSetter((host, value) -> host.setZ(value))
      .setValueGetter(host -> host.z)
      .setValueBuilder(new IntegerFactory<>(Integer.class))
      .setRequired(null)
      .build();
  public static FieldMetadataExtended<Location, LocationWritable, String> NAME_METADATA = FieldMetadataExtended
      .<Location, LocationWritable, String>builder()
      .setDisplayedName("Name")
      .setValueSetter((host, value) -> host.setName(value))
      .setValueGetter(host -> host.name)
      .setValueBuilder(new StringFactory())
      .setRequired(null)
      .build();

  public static <ParentHost> MetadataComposite<ParentHost, Location, LocationWritable> getMetadata(Function<ParentHost, LocationWritable> hostExtractor) {
    final List<FieldMetadataExtended<Location, LocationWritable, ?>> leafs = new ArrayList<>(); 
    leafs.add(X_METADATA);
    leafs.add(Y_METADATA);
    leafs.add(Z_METADATA);
    leafs.add(NAME_METADATA);
    
    return new MetadataComposite<>(TITLE, leafs, new ArrayList<>(), hostExtractor);
  }

  public static class Location {
    protected Float x;
    protected Double y;
    protected Integer z;
    protected String name;
  }

  public static class LocationWritable extends Location {
    public void setX(Float x) {
      this.x = x;
    }

    public void setY(Double y) {
      this.y = y;
    }

    public void setZ(Integer z) {
      this.z = z;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
