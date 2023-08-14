package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;

@NonNullByDefault
public class InfoCommand implements ICommand<CollectionMetadataComputed, ICollectionCommandReceiver<?>> {

  @Override
  public Observable<CollectionMetadataComputed> execute(ICollectionCommandReceiver<?> receiver) {
    return Observable.just(receiver.getMetadataTree());
  }
  
}
