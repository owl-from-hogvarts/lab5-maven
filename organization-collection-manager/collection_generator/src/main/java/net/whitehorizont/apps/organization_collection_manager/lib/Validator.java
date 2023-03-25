package net.whitehorizont.apps.organization_collection_manager.lib;

@FunctionalInterface
public interface Validator<V, T> {
  ValidationResult<Boolean> validate(V value, T t);
}
