package net.whitehorizont.libs.network.past;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ITransport {
  short getPacketLengthLimit();
  void send(byte[] packet);
}
