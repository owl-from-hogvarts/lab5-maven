package net.whitehorizont.apps.collection_manager.organisation.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;

@NonNullByDefault
public class GetDescendingCommand implements ICommand<Entry<ElementKey, OrganisationElementFull>, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {  
  @Override
  public Observable<Entry<ElementKey, OrganisationElementFull>> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().getDescending$();
  }
  
}
