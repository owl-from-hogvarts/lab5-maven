package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IWithAuthData;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class RemoveCommand implements IWithAuthData<ElementKey, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final UUID_ElementId id;

  public RemoveCommand(UUID_ElementId id) {
    this.id = id;
  }

  @Override
  public Observable<ElementKey> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
        return Observable.empty();
  }

  @Override
  public Observable<ElementKey> executeWithAuthData(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider, String login) {
    return dependencyProvider.getCollectionReceiver().removeById(login, id).toObservable();
  }
  
}
