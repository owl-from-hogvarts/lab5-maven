package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

public class OrganisationDataSink extends DataSinkSource<Builder, OrganisationElement, Collection<Builder, OrganisationElement>> {

  public OrganisationDataSink(Collection<Builder, OrganisationElement> validationObject) {
    super(validationObject);
  }

  @Override
  final protected OrganisationElement buildElementFrom(Builder prototype) throws ValidationError {
    return new OrganisationElement(getValidationObject(), prototype); 
  }
  
}
