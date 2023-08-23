package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.eclipse.jdt.annotation.NonNullByDefault;

// Manages transfers within single connection
// TODO: Rework to iterators
@NonNullByDefault
public class LongPackageFactory implements IPacketFactory {
  private static final int MAX_CONCURRENT_TRANSFERS_PER_CONNECTION = 100;

  private final Map<Long, TransferDescriptor> transfers = new HashMap<>();
  
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
      final var offset = payloadBuffer.position();
      payloadBuffer.get(chunk);
      final var packet = new Packet(new LongPackage(transferId, totalLength, offset, chunk));
      packets.add(packet);
    } 

    return (List<IPacket>) packets;
  }

  private short calcMaxPayloadSize() {
    return (short) (lengthLimit - LongPackage.getHeaderSize() - Packet.calcAllocatedSpace());
  }

  public Optional<byte[]> getCompletePackage(byte[] bytes) {
    final var packet = LongPackage.fromBytes(ByteBuffer.wrap(bytes));
    final var transfer = transfers.computeIfAbsent(packet.getTransferId(), transferId -> new TransferDescriptor(Math.toIntExact(packet.getTotalLength())));
    transfer.write(Math.toIntExact(packet.getOffset()), packet.getPayload());

    if (transfer.isFulfilled()) {
      transfers.remove(packet.getTransferId());
      return Optional.of(transfer.getFullPayload().array());
    }

    return Optional.empty();
  }

  public static class TransferDescriptor {
    // explicit
    private long bytesWritten = 0;
    private final  ByteBuffer fullPayload;

    public ByteBuffer getFullPayload() {
      return fullPayload;
    }

    TransferDescriptor(int totalLength) {
      this.fullPayload = ByteBuffer.allocate(totalLength);
    }

    void write(int offset, byte[] source) {
      fullPayload.put(offset, source);
      bytesWritten += source.length;
      assert bytesWritten <= fullPayload.capacity();
    }


    boolean isFulfilled() {
      return fullPayload.capacity() == bytesWritten;
    }
  }

}
