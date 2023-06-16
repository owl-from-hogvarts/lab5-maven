package net.whitehorizont.apps.organization_collection_manager.lib;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

public interface ICanSimpleValidate<Host> {
    void validate(Host host) throws ValidationError;
}
