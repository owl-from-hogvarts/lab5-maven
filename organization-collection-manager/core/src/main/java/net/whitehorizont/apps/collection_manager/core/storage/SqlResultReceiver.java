package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
@FunctionalInterface
public interface SqlResultReceiver {
  void receiveResult(ResultSet result) throws SQLException;
}
