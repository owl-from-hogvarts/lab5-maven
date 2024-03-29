package net.whitehorizont.libs.network.past;

import net.whitehorizont.libs.network.transport.udp.ReceiveTimeoutException;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ITransport<Endpoint> {
  short getSendPacketLengthLimit();
  void send(byte[] packet, Endpoint endpoint);
  TransportPacket<Endpoint> receive() throws ReceiveTimeoutException;
  void setTimeout(long timeoutMs);
}
