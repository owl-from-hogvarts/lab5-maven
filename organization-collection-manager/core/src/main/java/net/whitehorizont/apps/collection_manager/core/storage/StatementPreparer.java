package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
@FunctionalInterface
public interface StatementPreparer {
  void prepare(PreparedStatement statement) throws SQLException;
}
