package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class OrganisationElementFactory implements IElementFactory<OrganisationElementPrototype, OrganisationElement, ICollection<OrganisationElementPrototype, OrganisationElement>, UUID_ElementId> {

  // return new OrganisationElement(validationObject, prototype);
  private static final String COLLECTION_TYPE = OrganisationElement.ELEMENT_TITLE;

  @Override
  public OrganisationElementPrototype getElementPrototype() {
    return new OrganisationElementPrototype();
  }

  @Override
  public OrganisationElement buildElementFrom(OrganisationElementPrototype prototype,
      ICollection<OrganisationElementPrototype, OrganisationElement> validationObject) throws ValidationError {
        return new OrganisationElement(validationObject, prototype);
      }

  @Override
  public UUID_ElementId getElementId(String idString) throws ValidationError {
    return new UUID_ElementId(idString);
  }

  @Override
  public String getCollectionType() {
    return COLLECTION_TYPE;
  }

      
}
