package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;

@NonNullByDefault
public class SaveCommand implements ICommand<Void> {

  private final CollectionManagerReceiver<?, ?> collectionManagerReceiver;
  private final ICollection<?, ?, ?> collection;

  public SaveCommand(CollectionManagerReceiver<?, ?> collectionManagerReceiver, ICollection<?, ?, ?> collection) {
    this.collectionManagerReceiver = collectionManagerReceiver;
    this.collection = collection;
  }

  @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      collectionManagerReceiver.save(collection.getMetadataSnapshot().getId());
      subscriber.onComplete();
    });
    
  }
  
}
