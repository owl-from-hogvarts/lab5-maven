package net.whitehorizont.apps.collection_manager.core.commands;


import java.util.ArrayList;
import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;
import net.whitehorizont.libs.network.past.IConnection;
import net.whitehorizont.libs.network.past.INetworkPackager;
import net.whitehorizont.libs.network.serialize.SerializeManager;

@NonNullByDefault
public class CommandQueue<DependencyManager, Endpoint> implements ICommandQueue<DependencyManager> {
  private final SerializeManager serializer = new SerializeManager();
  private final Subject<ConnectableObservable<?>> commands = PublishSubject.create();
  private final DependencyManager dependencyManager;
  private final INetworkPackager<Endpoint> network;


  public CommandQueue(DependencyManager dependencyManager, INetworkPackager<Endpoint> network) {
    commands.subscribe(this::executeNext);
    this.dependencyManager = dependencyManager;
    this.network = network;
  }

  @Override
  public <@NonNull T> Observable<T> push(ICommand<T, ? super DependencyManager> command) throws EPERM {
    if (command.isServerOnly()) {
      throw new EPERM();
    }
    
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

  /**
   * !!! CLIENTS SHOULD HAVE NO WAY TO CALL THIS !!!
   * 
   * By pass command security checks
   * 
   * @return
   */
  public <@NonNull T> Observable<T> pushServer(ICommand<T, ? super DependencyManager> command) {
    return Observable.create((subscriber) -> {

      final var execution$ = command.execute(dependencyManager).publish();
      // this does not immediately start execution of observable
      // to start actual execution call connect
      execution$.subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);
      commands.onNext(execution$);
    });
  }

  public Observable<Void> terminate() {
    // TODO: finish
    try {
      commands.onComplete();
    } catch (Exception e) {
      // termination, so no more interested in errors
    }

    return Observable.empty();
  }

  public void start() {
    while (true) {
      final var connection = network.poll();
      final var payloads = connection.getPayloads();
      for (final var payload : payloads) {
        try {
          executeCommand(payload, connection);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException();
        }
      }
    }
  }

  private <T> void executeCommand(byte[] payload, IConnection<?> connection) throws ClassNotFoundException {
      final ICommand<T, DependencyManager> command = (ICommand<T, DependencyManager>) serializer.deserialize(payload);
      push(command)
        .toList()
        .map(responseList -> serializer.serialize((ArrayList) responseList))
        .subscribe(responseBytes -> connection.send(responseBytes), error -> error.printStackTrace());
  }
}
