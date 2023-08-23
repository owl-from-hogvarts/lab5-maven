package net.whitehorizont.libs.network.past;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
interface IPacketFactory {
  Iterable<IPacket> buildPackets(byte[] payload);
  Optional<byte[]> getCompletePackage(byte[] packet);
}
