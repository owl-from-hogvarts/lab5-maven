package net.whitehorizont.libs.network.past;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ITransport<Endpoint> {
  short getSendPacketLengthLimit();
  void send(byte[] packet, Endpoint endpoint);
  TransportPacket<Endpoint> receive();
}
