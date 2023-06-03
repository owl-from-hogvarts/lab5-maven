package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver.IPrototypeCallback;

@NonNullByDefault
public class UpdateCommand<P extends IElementPrototype<?>> implements ICommand<Void> {
  private final CollectionCommandReceiver<P,?> collection;
  private final BaseId id;
  private final IPrototypeCallback<P> callback;

  public UpdateCommand(BaseId id, CollectionCommandReceiver<P,?> collection, IPrototypeCallback<P> callback) {
    this.collection = collection;
    this.id = id;
    this.callback = callback;
    
  }

  @Override
  public Observable<Void> execute() {
    return collection.replace(id, callback);
  }
}
