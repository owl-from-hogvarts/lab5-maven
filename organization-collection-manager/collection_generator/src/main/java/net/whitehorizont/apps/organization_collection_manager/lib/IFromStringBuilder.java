package net.whitehorizont.apps.organization_collection_manager.lib;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

public interface IFromStringBuilder<V> {
  V buildFromString(String string) throws ValidationError;
}
