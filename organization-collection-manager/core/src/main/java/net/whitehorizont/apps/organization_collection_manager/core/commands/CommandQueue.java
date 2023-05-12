package net.whitehorizont.apps.organization_collection_manager.core.commands;


import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

@NonNullByDefault
public class CommandQueue {
  private final Subject<ConnectableObservable<?>> commands = PublishSubject.create();

  public CommandQueue() {
    commands.subscribe(this::executeNext);
  }

  public <@NonNull T> Observable<T> push(ICommand<T> command) {
    return Observable.create((subscriber) -> {

      final var execution$ = command.execute().publish();
      // this does not immediately start execution of observable
      // to start actual execution call connect
      execution$.subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);
      commands.onNext(execution$);
    });
  }

  private void executeNext(ConnectableObservable<?> command) {
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
