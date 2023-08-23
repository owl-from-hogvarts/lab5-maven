package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

// The goal is to manage connections
@NonNullByDefault
public class Past<Endpoint> {
  // MS stands for milliseconds
  private static final long RECEIVE_TIMEOUT_MS = 1000;
  // while protocol does not yet limit maximum concurrency
  // this implementation imposes restrictions.
  // Google's QUIC recommends not less than 100 concurrent streams
  private static final int START_CONNECTIONS_AMOUNT = 100;
  private static final int MAX_PACKET_SIZE = 1<<16;

  private final ITransport<Endpoint> transport;

  private final List<Consumer<Socket>> callbacks = new ArrayList<>();

  // key: Endpoint, { value: key: transferId, value: transferDescriptor }
  private final Map<Endpoint, Connection<Endpoint>> connections = new HashMap<>(START_CONNECTIONS_AMOUNT);

  public Past(ITransport<Endpoint> transport) {
    this.transport = transport;
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

  public void onRequest(Consumer<Socket> callback) {
    this.callbacks.add(callback);
  }

  public void start() {
    // we will utilize futures, or even observables to handle
    // multithreading.
    // Until that, callback should not last long, or datagram loss will happen
    
    // await on transport until data is available or timeout exceeds
    final byte[] data = new byte[MAX_PACKET_SIZE];
    final Endpoint endpoint = transport.receive(data);

    final var connection = connections.computeIfAbsent(endpoint, lambdaEndpoint -> {
      final Connection<Endpoint> lambdaConnection = new Connection<>(transport.getPacketLengthLimit(), new EndpointTransport<>(lambdaEndpoint, transport), this.callbacks);
      return lambdaConnection;
    });
    connection.receive(data);

    // now long package factory tracks transfers
    // create set of package factories for each connection
    // simple package factory instance can be shared because it does not have any state

    // as we can't determine a layer to handle connections
    // Let's try other approach
    // What if we wan't to extend definition of connection?
    // What if we add connection id or something like this to packets
    // Then it becomes clear that connection identity recognition is logical layer chores
    // Transport layer may provide some hints on connection data.
    // Logical layer will operate on opaque objects which are meaningful only for transport layer
    // They represent other end of communication, that is entity to which logical layer will send responses
    // The endpoint opaque object must be unique as it may be used to represent connection all alone

    // how to handle connections?
    // on transport layer
    // requires to create separate instances of packager for every connection

    // on logical layer
    // transport layer returns opaque handle which we should use when sending data back
    // for every connection requires to hold list of open streams

    // --------------- NEW CODE TO HANDLE INCOMING REQUESTS ---------------
    // start server
    // if new client encountered (this is determined by transport layer) onConnection method is called
    // onConnection setup listeners like onData
    // new Past(new UDPServerTransport())
    // !within sendCallback
    // transport.send(endpoint, payload)
    // !within Past
    // endpoint = transport.getData()
    // connection = connections.getOrDefault(endpoint, new Connection(payload -> transport.send(endpoint, payload)))
    // connection.handle(data)
    // !within connection's handle
    // new Factory(callback)
    // packet = Packet.fromBytes(data)
    // packetFactory = packetTypeMap.get(packet.getType())
    // packetFactory.fromBytes(packet.getPayload())
    // !within packetFactory's fromBytes
    // packet = LongPackage.fromBytes(bytes)
    // transfer = transfers.getOrDefault(packet.getTransferId(), new TransferDescriptor(callback))
    // transfer.write(packet.getOffset(), packet.getPayload())
    // if transfer.isComplete()
    //   transfer.callback.call(transfer)

    // requirements for callback arguments
    // - get payload
    // - send back
    // - send error
    //  or do nothing

    // then argument should contain at least endpoint

    // --------------- USER SIDE ---------------
    // new Past(new UDPDatagramChannelServer()).onRequest(connection -> {
    //   command = serializer.deserialize(payload);
    //   commandQueue.push(command).subscribe(result -> {
    //      resultBytes = serializer.serialize(result)
    //      connection.send(resultBytes)
    //   })
    // })
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
    private final ITransport<Endpoint> transport;
    
    public EndpointTransport(Endpoint endpoint, ITransport<Endpoint> transport) {
      this.endpoint = endpoint;
      this.transport = transport;
    }

    void send(byte[] packet) {
      transport.send(packet, endpoint);
    }

    short getPacketLengthLimit() {
      return transport.getPacketLengthLimit();
    }
  }
}
