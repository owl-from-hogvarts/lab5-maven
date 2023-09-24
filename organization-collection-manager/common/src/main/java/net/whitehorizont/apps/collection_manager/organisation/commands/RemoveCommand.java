package net.whitehorizont.apps.collection_manager.organisation.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class RemoveCommand implements ICommand<ElementKey, IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver>> {
  private final UUID_ElementId id;

  public RemoveCommand(UUID_ElementId id) {
    this.id = id;
  }

  @Override
  public Observable<ElementKey> execute(
      IProvideCollectionReceiver<? extends IOrganisationCollectionCommandReceiver> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().removeById(id).toObservable();
  }
  
}
