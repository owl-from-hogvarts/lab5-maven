package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended;
import net.whitehorizont.apps.organization_collection_manager.lib.IWritableHostFactory;
import net.whitehorizont.apps.organization_collection_manager.lib.MetadataComposite;
import net.whitehorizont.apps.organization_collection_manager.lib.SQLReader;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Hardcoded implementation for postgresql.
 * 
 * I'm too tired to implement extensible architecture here :)
 */
@NonNullByDefault
public class DatabaseStorage<Host extends ICollectionElement<Host>, WritableHost extends Host>
    implements IStorage<ICollection<Host>> {

  private final IWritableHostFactory<WritableHost> elementFactory;
  private final DatabaseConnectionFactory connectionFactory;
  private final RamCollection.Configuration<Host, ?> collectionFactory;

  public DatabaseStorage(DatabaseConnectionFactory connectionFactory, IWritableHostFactory<WritableHost> elementFactory,
      RamCollection.Configuration<Host, ?> collectionFactory) {
    this.connectionFactory = connectionFactory;
    this.elementFactory = elementFactory;
    this.collectionFactory = collectionFactory;
  }

  // preconfigure database connection or connection factory
  //

  // from now on collection is persistent
  // no need to create it programmatically

  private RamCollection<Host> createCollectionInstance() {
    final var collection = collectionFactory.build();

    return collection;
  }

  @Override
  public Observable<ICollection<Host>> load() {
    return Observable.create(subscriber -> {
      // request collection elements from database
      safeExecuteQuery("SELECT * FROM organisations", null, resultSet -> {
        try {
        final var collection = createCollectionInstance();
        // populate collection with received elements
        while (resultSet.next()) {
          final var element = elementFactory.createWritable();
          fillElement(resultSet, (MetadataComposite<?, Host, WritableHost>) OrganisationElementDefinition.getMetadata(),
              element);
          collection.insert(element);
        }
        // just return collection

        subscriber.onNext(collection);
        subscriber.onComplete();          
        } catch (SQLException|ValidationError|DuplicateElements|KeyGenerationError e) {
          subscriber.onError(e);
        }
      });
    });
  }

  private <ParentHost, Host, WritableHost extends Host> void fillElement(ResultSet resultSet,
      MetadataComposite<ParentHost, Host, WritableHost> metadata, WritableHost element) {
    for (final var field : metadata.getLeafs()) {
      processSqlForField(resultSet, element, field);
    }

    for (final var child : metadata.getChildren()) {
      doForChild(resultSet, child, element);
    }
  }

  private <ParentHost, Host, WritableHost extends Host> void doForChild(ResultSet resultSet,
      MetadataComposite<ParentHost, Host, WritableHost> childMetadata, ParentHost host) {
    final var childHost = childMetadata.extractChildHost(host);
    fillElement(resultSet, childMetadata, childHost);
  }

  private <Host, WritableHost extends Host, V> void processSqlForField(ResultSet resultSet, WritableHost element,
      FieldMetadataExtended<Host, WritableHost, V> field) {
    final Optional<SQLReader<V>> sqlReaderMaybe = field.getSQLReader();
    if (sqlReaderMaybe.isEmpty()) {
      return;
    }
    final var sqlReader = sqlReaderMaybe.get();

    try {
      final var value = sqlReader.read(resultSet);
      field.getValueSetter().accept(element, value);
    } catch (SQLException|ValidationError e) {
      throw new RuntimeException(e);
    }
  }

  private void safeExecuteQuery(String sqlTemplateString,
      @Nullable Consumer<PreparedStatement> sqlStatementPreparer, Consumer<ResultSet> callback)
      throws StorageInaccessibleError {
    try (final Connection db = connectionFactory.getConnection();
        PreparedStatement statement = db.prepareStatement(sqlTemplateString)) {
      if (sqlStatementPreparer != null) {
        sqlStatementPreparer.accept(statement);
      }
      final var resultSet = statement.executeQuery();
      callback.accept(resultSet);
    } catch (SQLException e) {
      throw new StorageInaccessibleError(e);
    }
  }

  @Override
  public Observable<CollectionMetadata> loadMetadata() throws CollectionNotFound {
    // fuck that)
    throw new UnsupportedOperationException("Collection metadata could not be retrieved in separate from collection");
  }

  @Override
  public void save(ICollection<Host> collection) throws StorageInaccessibleError {
    throw new UnsupportedOperationException("Collection is always actual");
  }

}
