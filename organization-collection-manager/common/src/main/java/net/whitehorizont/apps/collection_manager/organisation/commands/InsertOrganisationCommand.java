package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IWithAuthData;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;

@NonNullByDefault
public class InsertOrganisationCommand implements IWithAuthData<Void, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {

  private final OrganisationElementWritable element;
  private final String key;

  public InsertOrganisationCommand(String key, OrganisationElementWritable element) {
    this.element = element;
    this.key = key;
  }

  @Override
  public Observable<Void> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
        // do nothing when auth data is not available
        // owner is mandatory field
        return Observable.empty();
      }

  @Override
  public Observable<Void> executeWithAuthData(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider, String login) {
        return Observable.create(subscriber -> {
          dependencyProvider.getCollectionReceiver().insert(login, key, element);
          subscriber.onComplete();
        });
      }
  
}
