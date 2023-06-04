package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;

@NonNullByDefault
public class InfoCommand implements ICommand<TitledNode<ReadonlyField<?>>> {

  private final CollectionCommandReceiver<?, ?> collection;

  public InfoCommand(CollectionCommandReceiver<?, ?> collection) {
    this.collection = collection;
  }

  @Override
  public Observable<TitledNode<ReadonlyField<?>>> execute() {
    return Observable.just(collection.getMetadataSnapshot().getTree());
  }
  
}
