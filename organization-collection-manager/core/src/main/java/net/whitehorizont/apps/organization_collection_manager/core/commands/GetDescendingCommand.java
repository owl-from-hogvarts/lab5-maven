package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;

@NonNullByDefault
public class GetDescendingCommand implements ICommand<Entry<ElementKey, OrganisationElement>> {
  private final OrganisationCollectionCommandReceiver collection;
  

  public GetDescendingCommand(OrganisationCollectionCommandReceiver collection) {
    this.collection = collection;
  }

  @Override
  public Observable<Entry<ElementKey, OrganisationElement>> execute() {
    return collection.getDescending$();
  }
  
}
