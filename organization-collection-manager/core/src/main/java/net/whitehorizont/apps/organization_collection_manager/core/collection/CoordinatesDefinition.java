package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.IntegerFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;

@NonNullByDefault
public class CoordinatesDefinition {
  private static final String COORDINATES_TITLE = "Coordinates";
  
  private static final FieldMetadataExtended<Coordinates, CoordinatesWriteable, Integer, Object> X_METADATA = new FieldMetadataExtended.Metadata<Coordinates, CoordinatesWriteable, Integer, Object>().addValidator((value, _unused) -> {
    final int x = value.intValue();
    final var isValueOk = x > -802;
    final var result = new ValidationResult<>(isValueOk, "Value should be strictly above -802");
    return result;
  }).setDisplayedName("X")
  .setValueBuilder(new IntegerFactory())
  .setValueSetter((host, value) -> host.setX(value))
  .setValueGetter((host) -> host.getX())
  .build();


  public static class Coordinates {
    protected int x;
    private int getX() {
      return x;
    }
    protected int y;
    private int getY() {
      return y;
    }
  }

  public static class CoordinatesWriteable extends Coordinates {
    private CoordinatesWriteable setX(int x) {
      this.x = x;
      return this;
    }

    private CoordinatesWriteable setY(int y) {
      this.y = y;
      return this;
    }

  }

  public static <ParentHost> MetadataComposite<ParentHost, Coordinates, CoordinatesWriteable, ?> getTree(Function<ParentHost, Coordinates> coordinatesExtractor) {
    final List<FieldMetadataExtended<Coordinates, CoordinatesWriteable, ?, Object>> leafs = new ArrayList<>();
    leafs.add(X_METADATA);

    return new MetadataComposite<>(COORDINATES_TITLE, leafs, new ArrayList<>(), coordinatesExtractor);
  }
}
