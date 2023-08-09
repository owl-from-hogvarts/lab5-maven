package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class SaveCommand implements ICommand<Void> {

  private final CollectionManagerReceiver<?> collectionManagerReceiver;

  public SaveCommand(CollectionManagerReceiver<?> collectionManagerReceiver) {
    this.collectionManagerReceiver = collectionManagerReceiver;
  }

  @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      collectionManagerReceiver.getCollection().subscribe(collection -> {
        collectionManagerReceiver.save(collection.getPersistentMetadata().getId());
        subscriber.onComplete();
      });
    });
    
  }
  
}
