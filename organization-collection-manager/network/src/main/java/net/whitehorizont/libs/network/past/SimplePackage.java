package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;

// depending on length limit may be as large as 2^64 bytes
public class SimplePackage implements IPacket {
  private static final byte SIMPLE_PACKAGE_TYPE = 1;
  
  public static SimplePackage fromBytes(ByteBuffer bytes) {
    final var length = bytes.getShort();
    final var payload = new byte[length];
    bytes.get(payload);

    return new SimplePackage(payload);
  }

  public static byte getPacketType() {
    return SIMPLE_PACKAGE_TYPE;
  }

  // size in bits
  private static final byte LENGTH_FIELD_SIZE = 16;
  
  private final byte[] payload;

  public byte[] getPayload() {
    return payload;
  }

  /**
   * Implementation limitation: maximum payload size is 2Gb (2**31 bytes)
   * 
   * To send high volumes of data (such as huge files), use stream packet type
   */
  SimplePackage(byte[] payload) {
    this.payload = payload;
  }

  @Override
  public byte[] toBytes() {
    final var packetBuffer = ByteBuffer.allocate(calcPacketLength());
    // order matters
    writeHeader(packetBuffer);
    writePayload(packetBuffer);
    return packetBuffer.array();
  }

  private ByteBuffer writePayload(ByteBuffer packetBuffer) {
    return packetBuffer.put(payload);

  }

  private ByteBuffer writeHeader(ByteBuffer packetBuffer) {
    // cast just strips excess bits, leaving the rest unchanged
    return packetBuffer.putShort((short) payload.length);
  }

  @Override
  public short calcPacketLength() {
    return (short) (IPacket.bitsToBytes(LENGTH_FIELD_SIZE) + payload.length);
  }

  @Override
  public byte getType() {
    return SIMPLE_PACKAGE_TYPE;
  }
}
