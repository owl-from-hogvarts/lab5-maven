package net.whitehorizont.apps.collection_manager.core.commands;


import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;

@NonNullByDefault
public class CommandQueue<DependencyManager> implements ICommandQueue<DependencyManager> {
  private final Subject<ConnectableObservable<?>> commands = PublishSubject.create();
  private final DependencyManager dependencyManager;


  public CommandQueue(DependencyManager dependencyManager) {
    commands.subscribe(this::executeNext);
    this.dependencyManager = dependencyManager;
  }

  @Override
  public <@NonNull T> Observable<T> push(ICommand<T, ? super DependencyManager> command) {
    return Observable.create((subscriber) -> {

      final var execution$ = command.execute(dependencyManager).publish();
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
