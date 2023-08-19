package net.whitehorizont.libs.network.past;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class Past {
  private final IPacketFactory longPackageFactory;
  private final IPacketFactory simplePackageFactory;
  private final ITransport transport;

  public Past(ITransport transport) {
    final short lengthLimit = transport.getPacketLengthLimit();

    this.longPackageFactory = new LongPackageFactory(lengthLimit);
    this.simplePackageFactory = new SimplePackageFactory();

    this.transport = transport;
  }

  // control packets are special and are not directly sent by
  // upper layer
  // they are implementation detail of protocol and are
  // hidden from user

  // WHOLE PACKAGE AT ONCE
  void send(byte[] payload) {
    // take payload bytes
    // check payload's length
    // depending on length limit set, choose packet type
    // if package size exceeds limit, choose LONG_PACKAGE
    // else, choose SIMPLE_PACKAGE
    final IPacketFactory factory = isExceedsLengthLimit(payload) ? longPackageFactory : simplePackageFactory;

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

      transport.send(packetBytes);
    }

  }

  private boolean isExceedsLengthLimit(byte[] bytes) {
    return bytes.length > transport.getPacketLengthLimit();
  }

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

  // RECEIVE
  // await on transport until data is available or timeout exceeds
  // return readable channel with size
  // on read request depending on mode:
    // receive package
    // extract payload
    // return payload immediately or store it internal buffer until all package data
  // is received
  // if total bytes received does not match total length after
  // all calls to read were spent, or last read call does not return
  // too long, assume packet loss
  // send control packet with offset and length of lost part specified
  // if lost data is not recovered after 3 tires, give up
}
