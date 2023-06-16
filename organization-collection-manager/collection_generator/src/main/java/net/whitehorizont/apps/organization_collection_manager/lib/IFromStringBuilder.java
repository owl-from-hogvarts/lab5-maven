package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface IFromStringBuilder<V> {
  /** NullPointerException if any is intercepted and interpreted as null value */
  V buildFromString(@Nullable String string) throws ValidationError;
}
