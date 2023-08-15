package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationType;

@NonNullByDefault
public class CountCommand implements ICommand<Long, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final OrganisationType type;

  public CountCommand(OrganisationType type) {
    this.type = type;
  }

  @Override
  public Observable<Long> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().countByType(type).toObservable();
  }
  
}
