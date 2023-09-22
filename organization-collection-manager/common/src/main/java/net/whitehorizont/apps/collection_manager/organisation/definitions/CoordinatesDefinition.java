package net.whitehorizont.apps.collection_manager.organisation.definitions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.factories.IntegerFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationResult;

@NonNullByDefault
public class CoordinatesDefinition {
  private static final String COORDINATES_TITLE = "Coordinates";

  public static final FieldMetadataExtended<Coordinates, CoordinatesWriteable, Integer> X_METADATA = FieldMetadataExtended
      .<Coordinates, CoordinatesWriteable, Integer>builder()
      .addSimpleValidator(value -> {
        final int x = value.intValue();
        final var isValueOk = x > -802;
        final var result = new ValidationResult<>(isValueOk, "Value should be strictly above -802");
        return result;
      })
      .setDisplayedName("X")
      .setSQLReader(resultSet -> resultSet.getInt("coordinates_x"))
      .setValueBuilder(new IntegerFactory<>(Integer.class))
      .setValueSetter((host, value) -> host.setX(value))
      .setValueGetter((host) -> host.getX())
      .setRequired("X must be provided!")
      .build();

  public static final FieldMetadataExtended<Coordinates, CoordinatesWriteable, Long> Y_METADATA = FieldMetadataExtended
      .<Coordinates, CoordinatesWriteable, Long>builder()
      .setDisplayedName("Y")
      .setSQLReader(resultSet -> resultSet.getLong("coordinates_y"))
      .addSimpleValidator(value -> new ValidationResult<>(value <= 688, "Value should be lower or equal to 688"))
      .setValueBuilder(new IntegerFactory<>(Long.class))
      .setValueSetter((host, value) -> host.setY(value))
      .setValueGetter(host -> host.getY())
      .setRequired("Y must be provided!")
      .build();

  public static class Coordinates implements Serializable {
    protected int x;

    private int getX() {
      return x;
    }

    protected long y;

    private long getY() {
      return y;
    }
  }

  public static class CoordinatesWriteable extends Coordinates {
    private CoordinatesWriteable setX(int x) {
      this.x = x;
      return this;
    }

    private CoordinatesWriteable setY(long y) {
      this.y = y;
      return this;
    }

  }

  public static <ParentHost> MetadataComposite<ParentHost, Coordinates, CoordinatesWriteable> getTree(
      Function<ParentHost, CoordinatesWriteable> coordinatesExtractor) {
    final List<FieldMetadataExtended<Coordinates, CoordinatesWriteable, ?>> leafs = new ArrayList<>();
    leafs.add(X_METADATA);
    leafs.add(Y_METADATA);

    return new MetadataComposite<>(COORDINATES_TITLE, leafs, new ArrayList<>(), coordinatesExtractor);
  }
}
