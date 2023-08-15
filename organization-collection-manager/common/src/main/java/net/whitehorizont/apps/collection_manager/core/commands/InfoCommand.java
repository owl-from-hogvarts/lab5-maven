package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class InfoCommand implements ICommand<CollectionMetadataComputed, IProvideCollectionReceiver<?>> {

  @Override
  public Observable<CollectionMetadataComputed> execute(IProvideCollectionReceiver<?> receiver) {
    return Observable.just(receiver.getCollectionReceiver().getMetadataTree());
  }
  
}
