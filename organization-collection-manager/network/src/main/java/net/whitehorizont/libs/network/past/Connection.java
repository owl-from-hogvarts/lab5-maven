package net.whitehorizont.libs.network.past;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import net.whitehorizont.libs.network.past.Past.EndpointTransport;

// Manages package factories within single connection
public class Connection<Endpoint> {
  private final List<IPacketFactory> factories = new ArrayList<>(10);
  // connection to the endpoint
  // replies should go here
  private final List<Consumer<Socket>> callbacks;
  private final EndpointTransport<Endpoint> endpointTransport;

  public Connection(short lengthLimit, EndpointTransport<Endpoint> endpointTransport, List<Consumer<Socket>> callbacks) {
    factories.add(0, null);
    factories.add(1, new SimplePackageFactory());
    factories.add(2, new LongPackageFactory(lengthLimit));

    this.endpointTransport = endpointTransport;
    this.callbacks = callbacks;
  }

  private boolean isExceedsLengthLimit(byte[] bytes) {
    return Integer.compareUnsigned(bytes.length, endpointTransport.getPacketLengthLimit()) > 0;
  }

  // WHOLE PACKAGE AT ONCE
  public void send(byte[] payload) {
    // take payload bytes
    // check payload's length
    // depending on length limit set, choose packet type
    // if package size exceeds limit, choose LONG_PACKAGE
    // else, choose SIMPLE_PACKAGE
    final IPacketFactory factory = isExceedsLengthLimit(payload) ? factories.get(LongPackage.getPacketType()) : factories.get(SimplePackage.getPacketType());

    // supply payload to chosen packet factory
    // take packets from factory until no more available
    // for each packet
    for (final IPacket packet : factory.buildPackets(payload)) {
      // transform packets into bytes
      final byte[] packetBytes = packet.toBytes();
      // ensure that total size of packet does not exceeds length limit
      if (isExceedsLengthLimit(packetBytes)) {
        // if happens -> mistake in a factory code
        assert false;
        throw new RuntimeException();
      }
      // pass it to transport level

      endpointTransport.send(packetBytes);
    }

  }

  public void receive(byte[] bytes) {
    final var packet = Packet.fromBytes(bytes);
    final Optional<byte[]> payloadMaybe = factories.get(packet.getType()).getCompletePackage(packet.getPayload());
    if (payloadMaybe.isPresent()) {
      callCallbacks(payloadMaybe.get());
    }
  }

  private void callCallbacks(byte[] payload) {
    
    for (final var callback : callbacks) {
      final var socket = new Socket(payload, this);
      callback.accept(socket);
    }
  }
}
