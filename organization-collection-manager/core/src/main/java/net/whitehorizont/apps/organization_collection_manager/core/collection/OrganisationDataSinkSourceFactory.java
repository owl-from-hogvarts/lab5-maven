package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;

public class OrganisationDataSinkSourceFactory implements
    IDataSinkSourceFactory<OrganisationElement.Builder, OrganisationElement, IBaseCollection<OrganisationElement.Builder, OrganisationElement, ?>> {

  @Override
  public DataSinkSource<Builder, OrganisationElement, IBaseCollection<Builder, OrganisationElement, ?>> getDataSinkSourceFor(IBaseCollection<OrganisationElement.Builder, OrganisationElement, ?> validationObject) {
    return new OrganisationDataSink(validationObject);
  }

}
