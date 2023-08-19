package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;

/**
 * General wrapper for any protocol packet
 * 
 * Defines general fields
 */
public class Packet implements IPacket {
  private static final byte VERSION_SIZE = 16;
  private static final byte TYPE_SIZE = 8;
  private final short version = 0;
  private final ITypedPacket typedPacket;

  public Packet(ITypedPacket typedPacket) {
    this.typedPacket = typedPacket;
  }

  @Override
  public byte[] toBytes() {
    final var packetBuffer = ByteBuffer.allocate(calcPacketLength());
    writeHeader(packetBuffer);
    writePayload(packetBuffer);

    return packetBuffer.array();
  }


  private ByteBuffer writePayload(ByteBuffer packetBuffer) {
    return packetBuffer.put(typedPacket.toBytes());
  }

  private ByteBuffer writeHeader(ByteBuffer packetBuffer) {
    packetBuffer.putShort(version);
    packetBuffer.put(typedPacket.getType());

    return packetBuffer;
  }

  @Override
  public short calcPacketLength() {
    return (short) (calcAllocatedSpace() + typedPacket.calcPacketLength());
  }

  public static short calcAllocatedSpace() {
    return calcSizeInBytes(calcHeaderSizeInBits());
  }

  private static short calcHeaderSizeInBits() {
    return VERSION_SIZE + TYPE_SIZE;
  }

  public static short calcSizeInBytes(int sizeInBits) {
    return (short) Math.ceil(sizeInBits / 8);
  }
  
}
