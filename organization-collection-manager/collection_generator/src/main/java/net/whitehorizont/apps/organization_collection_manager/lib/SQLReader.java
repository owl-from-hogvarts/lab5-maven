package net.whitehorizont.apps.organization_collection_manager.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@FunctionalInterface
public interface SQLReader<V> {
  V read(ResultSet resultSet) throws SQLException, ValidationError;
}
