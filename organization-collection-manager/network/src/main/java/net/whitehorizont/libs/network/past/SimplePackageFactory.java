package net.whitehorizont.libs.network.past;

import java.util.ArrayList;
import java.util.List;

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

}
