package net.whitehorizont.apps.organization_collection_manager.core.commands;


import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;

@NonNullByDefault
public class CommandQueue<CM extends ICollectionManager<C, ?>, C extends IBaseCollection<?, ?, ?>> {
  private final CM collectionManager;
  private final Subject<ConnectableObservable<?>> commands = PublishSubject.create();

  public CommandQueue(CM collectionManager) {
    this.collectionManager = collectionManager;
    commands.subscribe(this::executeNext);
  }

  public <@NonNull T> Observable<T> push(BaseCommand<T, ? super C> command) {
    return Observable.create((subscriber) -> {

      if (!command.hasPreferredCollection()) {
        // we are at power here so we decide witch collection to supply
        final var collection = collectionManager.getCollection();
        command.setCollection(collection);
      }
      final var execution$ = command.execute().publish();
      // this does not immediately start execution of observable
      // to start actual execution call connect
      execution$.subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);
      commands.onNext(execution$);
    });
  }

  public <@NonNull T> void push(ISystemCommand<T> command) {
    command.setCommandQueue(this);
    command.setCollectionManager(collectionManager);

    final var execution$ = command.execute().publish();
    commands.onNext(execution$);
  }

  public void executeNext(ConnectableObservable<?> command) {
    command.connect();
  }

  Observable<Void> terminate() {
    // TODO: finish
    try {
      commands.onComplete();
    } catch (Exception e) {
      // termination, so no more interested in errors
    }

    return Observable.empty();
  }

}
