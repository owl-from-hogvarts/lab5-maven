package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IWithAuthData;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver.RemovalCriteria;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class RemoveByRevenueCommand
    implements IWithAuthData<Void, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final RemovalCriteria removalCriteria;
  private final double targetValue;

  public RemoveByRevenueCommand(RemovalCriteria removalCriteria, double targetValue) {
    this.removalCriteria = removalCriteria;
    this.targetValue = targetValue;
  }

  @Override
  public Observable<Void> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
        return Observable.empty();
  }

  @Override
  public Observable<Void> executeWithAuthData(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider, String login) {
    try {
      dependencyProvider.getCollectionReceiver().removeByRevenue(login, removalCriteria, targetValue);
    } catch (ValidationError|StorageInaccessibleError e) {
      return Observable.error(e);
    }
    return Observable.empty();  }

}
