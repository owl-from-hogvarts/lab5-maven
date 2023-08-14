package net.whitehorizont.apps.collection_manager.organisation.definitions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;


@NonNullByDefault
public enum OrganisationType {
  COMMERCIAL,
  PUBLIC,
  GOVERNMENT,
  OPEN_JOINT_STOCK_COMPANY;

  public static String getHint() {
    final List<String> enumStrings = new ArrayList<>();
    for (final var value : OrganisationType.values()) {
      enumStrings.add(value.toString());
    }

    return "Field can be one of: " + String.join(", ", enumStrings);
  }
}
