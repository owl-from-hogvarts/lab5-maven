package net.whitehorizont.apps.collection_manager.core.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DatabaseConnectionFactory {
  private final String connectionUrl;
  private final String login;
  private final String password;

  public DatabaseConnectionFactory(
    String connectionUrl,
    String login,
    String password
  ) {
    this.connectionUrl = connectionUrl;
    this.login = login;
    this.password = password;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(this.connectionUrl, login, password);
  }
}
