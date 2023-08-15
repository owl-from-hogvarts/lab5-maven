package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionManagerReceiver;

@NonNullByDefault
public class SaveCommand implements ICommand<Void, IProvideCollectionManagerReceiver<?>> {
  @Override
  public Observable<Void> execute(IProvideCollectionManagerReceiver<?> dependencyProvider) {
    final var collectionManagerReceiver = dependencyProvider.getCollectionManagerReceiver();
    
    return Observable.create(subscriber -> {
      collectionManagerReceiver.getCollection().subscribe(collection -> {
        collectionManagerReceiver.save(collection.getPersistentMetadata().getId());
        subscriber.onComplete();
      });
    });
  }

}
