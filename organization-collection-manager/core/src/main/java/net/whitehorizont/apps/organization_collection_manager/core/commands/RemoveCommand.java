package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;

@NonNullByDefault
public class RemoveCommand implements ICommand<Long> {
  private final Optional<UUID_ElementId> id;
  private final OrganisationCollectionCommandReceiver collection;

  public RemoveCommand(OrganisationCollectionCommandReceiver collection, UUID_ElementId id) {
    this.id = Optional.of(id);
    this.collection = collection;
  }
  // public RemoveCommand(SortDirection sortDirection, int revenue) {}

  @Override
  public Observable<Long> execute() {
    this.collection.removeById(id.get());
    return Observable.empty();
  }
  
}
