package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@FunctionalInterface
@NonNullByDefault
public interface Validator<V, T> {
  ValidationResult<Boolean> validate(V value, T t);
}
