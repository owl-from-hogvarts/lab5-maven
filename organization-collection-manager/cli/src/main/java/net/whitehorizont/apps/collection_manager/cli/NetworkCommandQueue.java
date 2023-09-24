package net.whitehorizont.apps.collection_manager.cli;

import java.nio.ByteBuffer;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;
import net.whitehorizont.libs.network.past.IConnection;
import net.whitehorizont.libs.network.serialize.SerializeManager;
import net.whitehorizont.libs.network.serialize.UnparsableResponse;
import net.whitehorizont.libs.result.Result;

// DP is required only for type safety
@NonNullByDefault
public class NetworkCommandQueue<DP extends IProvideAuthReceiver> implements ICommandQueue<DP> {
  private final SerializeManager serializer = new SerializeManager();
  private final IConnection<?> connection;
  
  public NetworkCommandQueue(IConnection<?> connection) {
    this.connection = connection;
  }
  
  @Override
  public <@NonNull T> Observable<T> push(ICommand<T, ? super DP> command) {
    return Observable.just(serializeCommand(command))
        .flatMap(commandBytes -> sendCommand(commandBytes))
        .flatMap(responseBytes -> this.deserializeResponse(responseBytes));

  }

  private ByteBuffer serializeCommand(ICommand<?, ?> command) {
    return ByteBuffer.wrap(serializer.serialize(command));
  }

  private Observable<ByteBuffer> sendCommand(ByteBuffer commandBytes) {
    return Observable.create(subscriber -> {
      // method is synchronies
      connection.send(commandBytes.array());
      final var payloads = connection.await();

      for (final var payload : payloads) {
        subscriber.onNext(ByteBuffer.wrap(payload));
      }

      subscriber.onComplete();
    });

  }

  private <T> Observable<@NonNull T> deserializeResponse(ByteBuffer responseBytes) {
    try {
      final Result<List<@NonNull T>> response = (Result<List<T>>) serializer.deserialize(responseBytes.array());

      if (response.isError()) {
        return Observable.error(response.getError());
      }
      
      return Observable.fromIterable(response.getResult());
    } catch (ClassNotFoundException e) {
      return Observable.error(new UnparsableResponse(e));
    }
  }

}
