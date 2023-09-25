package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IWithAuthData;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class ClearOrganisationCommand implements IWithAuthData<Void, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {

  @Override
  public Observable<Void> execute(IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    return Observable.empty();
  }

  @Override
  public Observable<Void> executeWithAuthData(IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider, String login) {
    return Observable.create(subscriber -> {
      dependencyProvider.getCollectionReceiver().clear(login);
      subscriber.onComplete();
    });
  }
}
