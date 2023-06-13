package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class ClearCommand implements ICommand<Void> {
  private final CollectionCommandReceiver<?> collectionCommandReceiver;

  public ClearCommand(CollectionCommandReceiver<?> collectionCommandReceiver) {
    this.collectionCommandReceiver = collectionCommandReceiver;
  }

  @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      collectionCommandReceiver.clear();
      subscriber.onComplete();
    });
  }
}
