package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;

public class LongPackage implements ITypedPacket {
  private static final byte LONG_PACKAGE_TYPE = 2;
  
  private static final byte TRANSFER_ID_FIELD_SIZE = 64;
  private final long transferId;
  private static final byte TOTAL_LENGTH_FIELD_SIZE = 64;
  private final long totalLength;
  private static final byte OFFSET_FIELD_SIZE = 64;
  private final long offset;
  private static final byte LENGTH_FIELD_SIZE = 16;
  private final byte[] payload;

  public LongPackage(long transferId, long totalLength, long offset, byte[] payload) {
    this.transferId = transferId;
    this.totalLength = totalLength;
    this.offset = offset;
    this.payload = payload;
  }

  @Override
  public byte[] toBytes() {
    final var packetBuffer = ByteBuffer.allocate(calcPacketLength());
    writeHeaders(packetBuffer);
    writePayload(packetBuffer);

    return packetBuffer.array();
  }

  private ByteBuffer writePayload(ByteBuffer packetBuffer) {
    return packetBuffer.put(payload);
  }

  private ByteBuffer writeHeaders(ByteBuffer packetBuffer) {
    packetBuffer.putLong(transferId);
    packetBuffer.putLong(totalLength);
    packetBuffer.putLong(offset);
    packetBuffer.putShort((short) payload.length);

    return packetBuffer;
  }

  @Override
  public short calcPacketLength() {
    return (short) (getHeaderSize() + payload.length);
  }

  @Override
  public byte getType() {
    return LONG_PACKAGE_TYPE;
  }

  /**
   * 
   * @return amount of bytes
   */
  public static short getHeaderSize() {
    return (short) Math.ceil((TRANSFER_ID_FIELD_SIZE + TOTAL_LENGTH_FIELD_SIZE + OFFSET_FIELD_SIZE + LENGTH_FIELD_SIZE) / 8);
  }

}
