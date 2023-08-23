package net.whitehorizont.libs.network.past;

public class TransportPacket<Endpoint> {
  private final Endpoint source;
  private final byte[] payload;
  
  public TransportPacket(Endpoint source, byte[] payload) {
    this.source = source;
    this.payload = payload;
  }


  Endpoint source() {
    return source;
  }

  
  byte[] payload() {
    return payload;
  }
}
