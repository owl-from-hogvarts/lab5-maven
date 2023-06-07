package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver.RemovalCriteria;

@NonNullByDefault
public class RemoveByRevenue implements ICommand<Void> {
  private final OrganisationCollectionCommandReceiver collection;
  private final RemovalCriteria removalCriteria;
  private final double targetValue;

  public RemoveByRevenue(OrganisationCollectionCommandReceiver collection, RemovalCriteria removalCriteria, double targetValue) {
    this.collection = collection;
    this.removalCriteria = removalCriteria;
    this.targetValue = targetValue;
  }

  @Override
  public Observable<Void> execute() {
    collection.removeByRevenue(removalCriteria, targetValue);
    return Observable.empty();
  }


  
}
