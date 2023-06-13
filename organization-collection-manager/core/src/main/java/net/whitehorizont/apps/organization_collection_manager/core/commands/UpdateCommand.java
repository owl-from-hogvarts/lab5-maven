package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementDefinition.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver.IPrototypeCallback;

@NonNullByDefault
public class UpdateCommand implements ICommand<Void> {
  private final OrganisationCollectionCommandReceiver collection;
  private final BaseId id;
  private final IPrototypeCallback<OrganisationElementPrototype> callback;

  public UpdateCommand(BaseId id, OrganisationCollectionCommandReceiver collection, IPrototypeCallback<OrganisationElementPrototype> callback) {
    this.collection = collection;
    this.id = id;
    this.callback = callback;
    
  }

  @Override
  public Observable<Void> execute() {
    // how the fuck this works?
    return collection.replaceById(id, callback::apply);
  }
}
