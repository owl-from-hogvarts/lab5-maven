package net.whitehorizont.libs.network.past;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IPacket {
  /** Converts packet into series of bytes */
  byte[] toBytes();
  short calcPacketLength();
  byte getType();

  public static short bitsToBytes(int sizeInBits) {
    return (short) Math.ceil(sizeInBits / 8);
  }
}
