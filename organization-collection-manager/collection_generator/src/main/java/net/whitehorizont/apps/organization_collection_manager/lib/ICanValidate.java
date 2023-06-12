package net.whitehorizont.apps.organization_collection_manager.lib;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

public interface ICanValidate<Host, T> {
  void validate(Host host, T validationObject) throws ValidationError;
}
