package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Validates given {@code Host} against object {@code T}
 * 
 * Example use case: validate element of collection against collection, to check 
 * uniqueness of id within this collection
 * 
 */
@NonNullByDefault
public interface ICanRichValidate<Host, T> {
  void validate(Host host, T validationObject) throws ValidationError;
}
