package net.whitehorizont.apps.organization_collection_manager.lib.factories;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface IFromStringBuilder<V> {
  /** NullPointerException if any is intercepted and interpreted as null value */
  V buildFromString(String string) throws ValidationError;
}
