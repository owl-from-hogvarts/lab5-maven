package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.commands.ICommand;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;

@NonNullByDefault
public class UpdateCommand implements ICommand<Void> {
  private final OrganisationCollectionCommandReceiver collection;
  private final BaseId id;
  private final OrganisationElementWritable element;

  public UpdateCommand(BaseId id, OrganisationCollectionCommandReceiver collection, OrganisationElementWritable element) {
    this.collection = collection;
    this.id = id;
    this.element = element;
    
  }

  @Override
  public Observable<Void> execute() {
    // how the fuck this works?
    return collection.replaceById(id, element);
  }
}
