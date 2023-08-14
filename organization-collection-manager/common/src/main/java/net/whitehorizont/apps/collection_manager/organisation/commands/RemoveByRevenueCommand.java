package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.collection_manager.organisation.commands.OrganisationCollectionCommandReceiver.RemovalCriteria;

@NonNullByDefault
public class RemoveByRevenueCommand implements ICommand<Void> {
  private final OrganisationCollectionCommandReceiver collection;
  private final RemovalCriteria removalCriteria;
  private final double targetValue;

  public RemoveByRevenueCommand(OrganisationCollectionCommandReceiver collection, RemovalCriteria removalCriteria, double targetValue) {
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
