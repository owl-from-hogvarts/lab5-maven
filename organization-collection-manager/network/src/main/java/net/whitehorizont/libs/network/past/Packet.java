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
  private final byte type;
  private final byte[] payload;
  
  public byte[] getPayload() {
    return payload;
  }

  public Packet(IPacket typedPacket) {
    this(typedPacket.getType(), typedPacket.toBytes());
  }

  public Packet(byte type, byte[] payload) {
    this.type = type;
    this.payload = payload;
  }

  @Override
  public byte[] toBytes() {
    final var packetBuffer = ByteBuffer.allocate(calcPacketLength());
    writeHeader(packetBuffer);
    writePayload(packetBuffer);

    return packetBuffer.array();
  }


  private ByteBuffer writePayload(ByteBuffer packetBuffer) {
    return packetBuffer.put(payload);
  }

  private ByteBuffer writeHeader(ByteBuffer packetBuffer) {
    packetBuffer.putShort(version);
    packetBuffer.put(type);

    return packetBuffer;
  }

  @Override
  public short calcPacketLength() {
    return (short) (calcAllocatedSpace() + payload.length);
  }

  public static short calcAllocatedSpace() {
    return IPacket.bitsToBytes(calcHeaderSizeInBits());
  }

  private static short calcHeaderSizeInBits() {
    return VERSION_SIZE + TYPE_SIZE;
  }
 
  // whole array is considered a packet
  public static Packet fromBytes(byte[] bytes) {
    final var byteBuffer = ByteBuffer.wrap(bytes);
    final short version = byteBuffer.getShort();
    final byte type = byteBuffer.get();
    final byte[] payload = new byte[bytes.length - calcAllocatedSpace()];
    byteBuffer.get(payload);

    return new Packet(type, payload);
  }

  @Override
  public byte getType() {
    return type;
  }
}
