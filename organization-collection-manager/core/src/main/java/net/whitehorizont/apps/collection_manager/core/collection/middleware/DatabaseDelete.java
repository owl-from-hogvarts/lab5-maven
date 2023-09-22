package net.whitehorizont.apps.collection_manager.core.collection.middleware;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.storage.DatabaseConnectionFactory;
import net.whitehorizont.apps.collection_manager.core.storage.SqlUtils;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class DatabaseDelete implements CollectionMiddleware<OrganisationElementFull> {
  private final DatabaseConnectionFactory connectionFactory;

  public DatabaseDelete(DatabaseConnectionFactory connectionFactory) {
      this.connectionFactory = connectionFactory;
    }

  @Override
  public void accept(ICollection<OrganisationElementFull> collection, OrganisationElementFull element)
      throws ValidationError, StorageInaccessibleError {
    SqlUtils.safeExecuteQuery(connectionFactory, "DELETE FROM organisations WHERE id = ?", (statement) -> {
      statement.setObject(1, OrganisationElementDefinition.ID_METADATA.getValueGetter().apply(element).getUUID());
    }, null);
  }

}
