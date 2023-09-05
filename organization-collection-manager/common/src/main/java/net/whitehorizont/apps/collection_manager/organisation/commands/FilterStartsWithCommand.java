package net.whitehorizont.apps.collection_manager.organisation.commands;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;

@NonNullByDefault
public class FilterStartsWithCommand implements ICommand<Pair<ElementKey, OrganisationElementFull>, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final String prefix;

  public FilterStartsWithCommand(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public Observable<Pair<ElementKey, OrganisationElementFull>> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().getStartsWith$(prefix);
  }
  
}
