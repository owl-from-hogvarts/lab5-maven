package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class SimplePackageFactory implements IPacketFactory {
  @Override
  public Iterable<IPacket> buildPackets(byte[] payload) {
    final Packet packet = new Packet(new SimplePackage(payload));
    final List<IPacket> packets = new ArrayList<>();
    packets.add(packet);
    return packets;
  }

  @Override
  public Optional<byte[]> getCompletePackage(byte[] packet) {
    return Optional.of(SimplePackage.fromBytes(ByteBuffer.wrap(packet)).getPayload());
  }

}
