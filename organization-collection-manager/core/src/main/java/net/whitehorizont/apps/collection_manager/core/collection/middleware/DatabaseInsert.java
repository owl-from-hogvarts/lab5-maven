package net.whitehorizont.apps.collection_manager.core.collection.middleware;


import java.sql.Timestamp;
import java.util.UUID;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.storage.DatabaseConnectionFactory;
import net.whitehorizont.apps.collection_manager.core.storage.SqlUtils;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.definitions.AddressDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.CoordinatesDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.LocationDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class DatabaseInsert implements CollectionMiddleware<OrganisationElementFull> {
  private final DatabaseConnectionFactory connectionFactory;

  public DatabaseInsert(DatabaseConnectionFactory connectionFactory) {
      this.connectionFactory = connectionFactory;
    }


  @Override
  public void accept(ICollection<OrganisationElementFull> collection, OrganisationElementFull element)
      throws ValidationError, StorageInaccessibleError {
        SqlUtils.safeExecuteQuery(connectionFactory, "INSERT INTO organisations VALUES (?, ?, ?, ?::organisation_type, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", (statement) -> {
          // populate preparedStatement with actual data
          final String owner = OrganisationElementDefinition.OWNER_METADATA.getValueGetter().apply(element);
          statement.setString(1, owner);

          final UUID id = OrganisationElementDefinition.ID_METADATA.getValueGetter().apply(element).getUUID();
          statement.setObject(2, id);

          final String name = OrganisationElementDefinition.NAME_METADATA.getValueGetter().apply(element.getElement());
          statement.setString(3, name);

          final String org_type = OrganisationElementDefinition.TYPE_METADATA.getValueGetter().apply(element.getElement()).toString().toLowerCase();
          statement.setString(4, org_type);

          final double annual_turnover = OrganisationElementDefinition.ANNUAL_TURNOVER_METADATA.getValueGetter().apply(element.getElement());
          statement.setDouble(5, annual_turnover);

          final String full_name = OrganisationElementDefinition.FULL_NAME_METADATA.getValueGetter().apply(element.getElement());
          statement.setString(6, full_name);

          final Timestamp creationDate = Timestamp.valueOf(OrganisationElementDefinition.CREATION_DATE_METADATA.getValueGetter().apply(element));
          statement.setObject(7, creationDate);

          final int coordinates_x = CoordinatesDefinition.X_METADATA.getValueGetter().apply(element.getElement().getCoordinates());
          statement.setInt(8, coordinates_x);

          final long coordinates_y = CoordinatesDefinition.Y_METADATA.getValueGetter().apply(element.getElement().getCoordinates());
          statement.setLong(9, coordinates_y);

          final String address_street = AddressDefinition.STREET_METADATA.getValueGetter().apply(element.getElement().getAddress());
          statement.setString(10, address_street);

          final String address_zipcode = AddressDefinition.ZIP_CODE_METADATA.getValueGetter().apply(element.getElement().getAddress());
          statement.setString(11, address_zipcode);

          final float address_location_x = LocationDefinition.X_METADATA.getValueGetter().apply(element.getElement().getAddress().getTown());
          statement.setFloat(12, address_location_x);

          final double address_location_y = LocationDefinition.Y_METADATA.getValueGetter().apply(element.getElement().getAddress().getTown());
          statement.setDouble(13, address_location_y);

          final int address_location_z = LocationDefinition.Z_METADATA.getValueGetter().apply(element.getElement().getAddress().getTown());
          statement.setInt(14, address_location_z);

          final String address_location_name = LocationDefinition.NAME_METADATA.getValueGetter().apply(element.getElement().getAddress().getTown());
          statement.setString(15, address_location_name);



        }, null);
      }

  
}
