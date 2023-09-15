package net.whitehorizont.libs.network.past;

import java.util.HashMap;
import java.util.Map;

import net.whitehorizont.libs.network.transport.udp.ReceiveTimeoutException;
import org.eclipse.jdt.annotation.NonNullByDefault;

// The goal is to manage connections
@NonNullByDefault
public class Past<Endpoint> implements INetworkPackager<Endpoint> {
  // MS stands for milliseconds
  private static final long RECEIVE_TIMEOUT_MS = 1000;
  // while protocol does not yet limit maximum concurrency
  // this implementation imposes restrictions.
  // Google's QUIC recommends not less than 100 concurrent streams
  private static final int START_CONNECTIONS_AMOUNT = 100;
  private static final int MAX_PACKET_SIZE = 1<<16;

  private final ITransport<Endpoint> transport;

  // key: Endpoint, { value: key: transferId, value: transferDescriptor }
  private final Map<Endpoint, Connection<Endpoint>> connections = new HashMap<>(START_CONNECTIONS_AMOUNT);

  public Past(ITransport<Endpoint> transport) {
    this.transport = transport;
  }

  @Override
  public IConnection<Endpoint> connect(Endpoint endpoint) {
    return this.connections.computeIfAbsent(endpoint, endpointLambda -> new Connection<>(transport.getSendPacketLengthLimit(), new EndpointTransport<Endpoint>(endpoint, transport, this)));
  }



  // control packets are special and are not directly sent by
  // upper layer
  // they are implementation detail of protocol and are
  // hidden from user

  // separation of concerns occurs:
  // class Past manages control packet sequences (including error handling)
  // meanwhile delegating payload handling to packet factories

  // A problem arise when underlying transport does not
  // have length limit, but device has limited RAM or can't
  // send such a great chunk of data within single call to
  // transport layer.
  // SOLVED: specify absolute maximum SIMPLE_PACKAGE length

  // For too large data transfers, utilize streams. Regular api has
  // restriction on data length: no more than 2GiB

  @Override
  public IConnection<Endpoint> poll() throws ReceiveTimeoutException {
    // we will utilize futures, or even observables to handle
    // multithreading.
    // Until that, callback should not last long, or datagram loss will happen
    while (true) {
      final TransportPacket<Endpoint> transportPacket = transport.receive();
      final var connection = connections.computeIfAbsent(transportPacket.source(), lambdaEndpoint -> new Connection<>(transport.getSendPacketLengthLimit(), new EndpointTransport<>(lambdaEndpoint, transport, this)));
      if (connection.receive(transportPacket.payload())) {
        return connection;
      }


      // explicitly
    }

    // --------------- USER SIDE ---------------
    // past = new Past(new UDPDatagramChannelServer())
    // while (true)
    //   connection = past.poll()
    //   for (payload of collection.getPayloads())
    //     command = serializer.deserialize(payload)
    //     commandQueue.push(command).subscribe(result -> connection.send(serializer.serialize(result)))
  }

  // transform api back to imperative-classic
  // that is move callbacks from logical layer to application one
  // receive method should return connection instance when connection is ready to provide at least one complete package
  // connection instance should have source field, which is opaque
  // representation of transport layer address to which response should be sent
  // send method takes that source and data to send
  // connection should provide ready made packages/transfers
  // Rationale: this will massively simplify logical layer
  // and make callback handlings easier

  // connection = past.receive()
  // connection.getPackages() // returns all packages which are completed

  public static class EndpointTransport<Endpoint> {
    private final Endpoint endpoint;
    
    public Endpoint getEndpoint() {
      return endpoint;
    }

    private final ITransport<Endpoint> transport;
    private final Past<Endpoint> connectionManager;
    
    public EndpointTransport(Endpoint endpoint, ITransport<Endpoint> transport, Past<Endpoint> connectionManager) {
      this.endpoint = endpoint;
      this.transport = transport;
      this.connectionManager = connectionManager;
    }

    void send(byte[] packet) {
      transport.send(packet, endpoint);
    }

    short getPacketLengthLimit() {
      return transport.getSendPacketLengthLimit();
    }

    IConnection<Endpoint> poll() throws ReceiveTimeoutException {
      return connectionManager.poll();
    }
  }
}
