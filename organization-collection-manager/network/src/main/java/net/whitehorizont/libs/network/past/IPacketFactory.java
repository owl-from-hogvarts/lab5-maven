package net.whitehorizont.libs.network.past;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
interface IPacketFactory {
  Iterable<IPacket> buildPackets(byte[] payload);
}
