package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class SqlUtils {
  public static void safeExecuteQuery(DatabaseConnectionFactory connectionFactory, String sqlTemplateString,
      @Nullable StatementPreparer sqlStatementPreparer, @Nullable SqlResultReceiver callback)
      throws StorageInaccessibleError {
    try (final Connection db = connectionFactory.getConnection();
        PreparedStatement statement = db.prepareStatement(sqlTemplateString)) {
      if (sqlStatementPreparer != null) {
        sqlStatementPreparer.prepare(statement);
      }
      if (callback != null) {
        final var resultSet = statement.executeQuery();
        callback.receiveResult(resultSet);
        resultSet.close();
        return;
      }
      statement.execute();

    } catch (SQLException e) {
      throw new StorageInaccessibleError(e);
    }
  }
}
