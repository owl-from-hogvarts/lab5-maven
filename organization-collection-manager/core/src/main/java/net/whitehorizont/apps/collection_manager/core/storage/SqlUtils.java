package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

public class SqlUtils {
  public static void safeExecuteQuery(DatabaseConnectionFactory connectionFactory, String sqlTemplateString,
      @Nullable StatementPreparer sqlStatementPreparer, Consumer<ResultSet> callback)
      throws StorageInaccessibleError {
    try (final Connection db = connectionFactory.getConnection();
        PreparedStatement statement = db.prepareStatement(sqlTemplateString)) {
      if (sqlStatementPreparer != null) {
        sqlStatementPreparer.prepare(statement);
      }
      if (callback != null) {
        final var resultSet = statement.executeQuery();
        callback.accept(resultSet);
        return;
      }
      statement.execute();

    } catch (SQLException e) {
      throw new StorageInaccessibleError(e);
    }
  }
}
