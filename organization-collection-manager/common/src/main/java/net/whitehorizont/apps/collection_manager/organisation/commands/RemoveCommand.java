package net.whitehorizont.apps.collection_manager.organisation.commands;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class RemoveCommand implements ICommand<Long, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final Optional<UUID_ElementId> id;

  @SuppressWarnings("null")
  public RemoveCommand(UUID_ElementId id) {
    this.id = Optional.of(id);
  }

  @Override
  public Observable<Long> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    dependencyProvider.getCollectionReceiver().removeById(id.get());
    return Observable.empty();
  }
  
}
