package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;

@NonNullByDefault
public class UpdateCommand implements ICommand<Void, IProvideCollectionReceiver<IOrganisationCollectionCommandReceiver>> {
  private final BaseId id;
  private final OrganisationElementWritable element;

  public UpdateCommand(BaseId id, IOrganisationCollectionCommandReceiver collection, OrganisationElementWritable element) {
    this.id = id;
    this.element = element;
    
  }

  @Override
  public Observable<Void> execute(
      IProvideCollectionReceiver<IOrganisationCollectionCommandReceiver> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().replaceById(id, element);
  }
}
