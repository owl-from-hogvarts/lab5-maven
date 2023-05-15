package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementRawData;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

@NonNullByDefault
public class OrganisationElementFactory implements IElementFactory<OrganisationElementRawData, OrganisationElement, ICollection<OrganisationElementRawData, OrganisationElement, ?>> {

  @Override
  public OrganisationElement buildElementFrom(OrganisationElementRawData prototype,
      ICollection<OrganisationElementRawData, OrganisationElement, ?> validationObject) throws ValidationError {
        return new OrganisationElement(validationObject, prototype);
      }
  
}
