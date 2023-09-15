package net.whitehorizont.libs.network.past;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.libs.network.past.Past.EndpointTransport;
import net.whitehorizont.libs.network.transport.udp.ReceiveTimeoutException;

@NonNullByDefault
// Manages package factories within single connection
public class Connection<Endpoint> implements IConnection<Endpoint> {
  private final List<@Nullable IPacketFactory> factories = new ArrayList<>(10);
  // connection to the endpoint
  // replies should go here
  private final EndpointTransport<Endpoint> endpointTransport;
  private List<byte[]> completePayloads = new ArrayList<>();

  public Connection(short lengthLimit, EndpointTransport<Endpoint> endpointTransport) {
    System.out.println("Connection created");
    factories.add(0, null);
    factories.add(1, new SimplePackageFactory());
    factories.add(2, new LongPackageFactory(lengthLimit));

    this.endpointTransport = endpointTransport;
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
    assert factory != null;
    if (factory == null) {
      throw new RuntimeException();
    }
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

  /**
   * 
   * @param packetBytes
   * @return {@code true} means at least one data entity is available
   */
  boolean receive(byte[] packetBytes) {
    final var packet = Packet.fromBytes(packetBytes);
    final Optional<byte[]> payloadMaybe = factories.get(packet.getType()).getCompletePackage(packet.getPayload());

    if (payloadMaybe.isPresent()) {
      completePayloads.add(payloadMaybe.get());
    }

    return !completePayloads.isEmpty();
  }

  public List<byte[]> getPayloads() {
    final var completePayloads = this.completePayloads;
    this.completePayloads = new ArrayList<>();
    return completePayloads;
  }

  @Override
  public List<byte[]> await() throws ReceiveTimeoutException {
    // poll transport until this connection is selected
    while (true) {
      final var connection = endpointTransport.poll();
      if (connection.getEndpoint() == this.getEndpoint()) {
        return connection.getPayloads();
      }
    }
  }

  @Override
  public Endpoint getEndpoint() {
    return endpointTransport.getEndpoint();
  }
}
