package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationType;

@NonNullByDefault
public class CountCommand implements ICommand<Long> {
  private final OrganisationCollectionCommandReceiver collection;
  private final OrganisationType type;

  public CountCommand(OrganisationCollectionCommandReceiver collection, OrganisationType type) {
    this.collection = collection;
    this.type = type;
  }


  @Override
  public Observable<Long> execute() {
    return collection.countByType(type).toObservable();
  }
  
}
