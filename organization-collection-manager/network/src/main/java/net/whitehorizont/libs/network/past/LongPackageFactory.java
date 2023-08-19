package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNullByDefault;

// TODO: Rework to iterators
@NonNullByDefault
public class LongPackageFactory implements IPacketFactory {
  private final short lengthLimit;
  private final Random random = new Random(); 

  public LongPackageFactory(short lengthLimit) {
    this.lengthLimit = lengthLimit;
  }

  @Override
  public Iterable<IPacket> buildPackets(byte[] payload) {
    final var payloadBuffer = ByteBuffer.wrap(payload);
    // generate random id
    final long transferId = random.nextLong();
    final long totalLength = payload.length;
    
    final List<? super Packet> packets = new ArrayList<>();
    while (payloadBuffer.remaining() > 0) {
      final var chunk = new byte[Math.min(payloadBuffer.remaining(), calcMaxPayloadSize())];
      payloadBuffer.get(chunk);
      final var packet = new Packet(new LongPackage(transferId, totalLength, lengthLimit, chunk));
      packets.add(packet);
    } 

    return (List<IPacket>) packets;
  }

  private short calcMaxPayloadSize() {
    return (short) (lengthLimit - LongPackage.getHeaderSize() - Packet.calcAllocatedSpace());
  }

}
