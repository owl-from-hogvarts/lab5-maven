package net.whitehorizont.libs.network.past;

public class Socket {
  private final byte[] payload;
  private final Connection<?> sender;

  public Socket(byte[] payload, Connection<?> sender) {
    this.payload = payload;
    this.sender = sender;
  }


  public byte[] getPayload() {
    return payload;
  }


  public void send(byte[] payload) {
    sender.send(payload);
  }
}
