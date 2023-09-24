package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class SqlUtils {
  public static Optional<Integer> safeExecuteQuery(DatabaseConnectionFactory connectionFactory, String sqlTemplateString,
      @Nullable StatementPreparer sqlStatementPreparer, @Nullable SqlResultReceiver callback)
      throws StorageInaccessibleError {
    try (final Connection db = connectionFactory.getConnection();
        PreparedStatement statement = db.prepareStatement(sqlTemplateString)) {
      if (sqlStatementPreparer != null) {
        sqlStatementPreparer.prepare(statement);
      }
      final boolean isResultSet = statement.execute();
      if (isResultSet && (callback != null)) {
        final var resultSet = statement.getResultSet();
        callback.receiveResult(resultSet);
        resultSet.close();
        return Optional.empty();
      }

      final boolean isCount = !isResultSet;
      if (isCount) {
        return Optional.of(statement.getUpdateCount());
      }

      return Optional.empty();
    } catch (SQLException e) {
      throw new StorageInaccessibleError(e);
    }
  }
}
