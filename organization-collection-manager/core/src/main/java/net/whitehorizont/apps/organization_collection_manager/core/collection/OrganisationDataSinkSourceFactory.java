package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class OrganisationDataSinkSourceFactory implements
    IDataSinkSourceFactory<OrganisationElement.Builder, OrganisationElement, IBaseCollection<OrganisationElement.Builder, OrganisationElement, ?>> {

  @Override
  public DataSinkSource<OrganisationElement.Builder, OrganisationElement, IBaseCollection<OrganisationElement.Builder, OrganisationElement, ?>> getDataSinkSourceFor(IBaseCollection<OrganisationElement.Builder, OrganisationElement, ?> validationObject) {
    return new OrganisationDataSink(validationObject);
  }

}
