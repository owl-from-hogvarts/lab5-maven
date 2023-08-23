package net.whitehorizont.libs.network.past;

public interface IPacket {
  /** Converts packet into series of bytes */
  byte[] toBytes();
  short calcPacketLength();
  byte getType();
}
