package net.whitehorizont.apps.organization_collection_manager.lib;

@FunctionalInterface
public interface SimpleValidator<V> {
  ValidationResult<Boolean> validate(V value);
}
