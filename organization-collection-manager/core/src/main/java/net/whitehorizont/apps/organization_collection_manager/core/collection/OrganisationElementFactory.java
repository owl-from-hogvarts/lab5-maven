package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

@NonNullByDefault
public class OrganisationElementFactory implements IElementFactory<Builder, OrganisationElement, IBaseCollection<Builder, OrganisationElement, ?>> {

  @Override
  public OrganisationElement buildElementFrom(Builder prototype,
      IBaseCollection<Builder, OrganisationElement, ?> validationObject) throws ValidationError {
        return new OrganisationElement(validationObject, prototype);
      }
  
}
