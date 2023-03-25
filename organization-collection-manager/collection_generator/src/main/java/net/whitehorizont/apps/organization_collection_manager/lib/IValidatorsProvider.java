package net.whitehorizont.apps.organization_collection_manager.lib;

import java.util.List;

public interface IValidatorsProvider<V, T> {
  List<Validator<V, T>> getValidators();
  Validator<V, T> getNullCheckValidator();
}
