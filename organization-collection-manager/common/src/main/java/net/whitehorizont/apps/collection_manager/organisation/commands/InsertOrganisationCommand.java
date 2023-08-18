package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;

@NonNullByDefault
public class InsertOrganisationCommand implements ICommand<Void, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {

  private final OrganisationElementWritable element;
  private final String key;

  public InsertOrganisationCommand(String key, OrganisationElementWritable element) {
    this.element = element;
    this.key = key;
  }

  @Override
  public Observable<Void> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
        return Observable.create(subscriber -> {
          dependencyProvider.getCollectionReceiver().insert(key, element);
          subscriber.onComplete();
        });
      }
  
}
