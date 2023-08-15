package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver.RemovalCriteria;

@NonNullByDefault
public class RemoveByRevenueCommand
    implements ICommand<Void, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final RemovalCriteria removalCriteria;
  private final double targetValue;

  public RemoveByRevenueCommand(RemovalCriteria removalCriteria, double targetValue) {
    this.removalCriteria = removalCriteria;
    this.targetValue = targetValue;
  }

  @Override
  public Observable<Void> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    dependencyProvider.getCollectionReceiver().removeByRevenue(removalCriteria, targetValue);
    return Observable.empty();
  }

}
