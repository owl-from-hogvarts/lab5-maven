package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementRawData;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class OrganisationElementFactory implements IElementFactory<OrganisationElementPrototype, OrganisationElement, ICollection<OrganisationElementPrototype, OrganisationElement, ?>> {

  // return new OrganisationElement(validationObject, prototype);

  @Override
  public OrganisationElementPrototype getElementPrototype() {
    return new OrganisationElementPrototype();
  }

  @Override
  public OrganisationElement buildElementFrom(OrganisationElementPrototype prototype,
      ICollection<OrganisationElementPrototype, OrganisationElement, ?> validationObject) throws ValidationError {
        return new OrganisationElement(validationObject, prototype);
      }

      
}
