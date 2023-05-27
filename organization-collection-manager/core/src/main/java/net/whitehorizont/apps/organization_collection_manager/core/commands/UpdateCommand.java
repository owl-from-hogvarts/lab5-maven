package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;

@NonNullByDefault
public class UpdateCommand<P extends IElementPrototype<?>> implements ICommand<Void> {
  private final CollectionCommandReceiver<P,?,?> collection;
  private final P prototype;
  private final BaseId id;

  public UpdateCommand(BaseId id, P prototype, CollectionCommandReceiver<P,?,?> collection) {
    this.collection = collection;
    this.prototype = prototype;
    this.id = id;
  }

  @Override
  public Observable<Void> execute() {
    collection.replace(id, prototype);
    return Observable.empty();
  }
  
}
