package net.whitehorizont.apps.organization_collection_manager.lib.validators;

import java.util.List;

public interface IValidatorsProvider<V, T> {
  List<Validator<V, T>> getValidators();
  SimpleValidator<V> getNullCheckValidator();
}
