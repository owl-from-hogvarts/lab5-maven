package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;

@NonNullByDefault
public class InfoCommand implements ICommand<CollectionMetadataComputed> {

  private final CollectionCommandReceiver<?> collection;

  public InfoCommand(CollectionCommandReceiver<?> collection) {
    this.collection = collection;
  }

  @Override
  public Observable<CollectionMetadataComputed> execute() {
    return Observable.just(collection.getMetadataTree());
  }
  
}
