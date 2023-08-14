package net.whitehorizont.apps.collection_manager.organisation.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElement;

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
