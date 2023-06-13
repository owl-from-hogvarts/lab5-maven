package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementDefinition.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;

@NonNullByDefault
public class FilterStartsWithCommand implements ICommand<Entry<ElementKey, OrganisationElement>> {
  private final OrganisationCollectionCommandReceiver collection;
  private final String prefix;

  public FilterStartsWithCommand(OrganisationCollectionCommandReceiver collection, String prefix) {
    this.collection = collection;
    this.prefix = prefix;
  }

  @Override
  public Observable<Entry<ElementKey, OrganisationElement>> execute() {
    return collection.getStartsWith$(prefix);
  }
  
}
