package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class ClearCommand implements ICommand<Void, ICollectionCommandReceiver<?>> {

  @Override
  public Observable<Void> execute(ICollectionCommandReceiver<?> receiver) {
    return Observable.create(subscriber -> {
      receiver.clear();
      subscriber.onComplete();
    });
  }
}
