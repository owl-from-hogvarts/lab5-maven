package net.whitehorizont.libs.network.past;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class Test {
  public static void main(String[] args) {
    final String sent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque sollicitudin tortor sit amet nibh laoreet tempor. Curabitur tincidunt metus quam, sed vulputate nulla varius vitae. Proin sapien erat, porta dapibus arcu a, rutrum egestas nisi. Mauris lectus mi, dapibus eu dapibus sed, ultrices at dolor. Curabitur at eleifend felis. Mauris gravida consectetur cursus. Donec arcu risus, gravida eu risus vitae, hendrerit pharetra mauris. Nullam ut purus cursus, condimentum sapien finibus, maximus eros. Mauris id purus sit amet elit suscipit ultrices. Vivamus eget nisi ut lacus tempor rhoncus. In non sagittis dolor. In interdum est a mi tempor aliquet.\n"
              + //
              "\n" + //
              "Donec urna lacus, tincidunt vel magna ut, egestas lacinia neque. Pellentesque ullamcorper felis tellus, sed finibus justo mattis sed. Etiam cursus turpis et odio hendrerit scelerisque. Duis aliquet nunc cursus est tempus pretium. Donec eleifend blandit faucibus. Proin eget scelerisque nibh. Nunc luctus non nibh ut dictum. Nulla eu tempor lectus. Duis interdum, nulla vitae ultrices iaculis, neque orci semper enim, sit amet finibus ligula est vel risus. Nullam sapien urna, porttitor ac pretium vel, lacinia quis lorem. Proin id quam eget nisi ultrices sagittis vel eu nibh. Nulla quis metus et sem pretium sodales.\n"
              + //
              "\n" + //
              "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Proin vitae vestibulum mi. Aliquam venenatis, metus non consectetur laoreet, odio diam commodo orci, eget ornare tortor sem ut diam. Aenean ac lectus magna. Vivamus vestibulum ullamcorper risus, condimentum tempus leo commodo ut. Morbi efficitur sodales lectus viverra auctor. Maecenas ex ante, suscipit non mauris ornare, dapibus rutrum mauris. Praesent dictum maximus felis.\n"
              + //
              "\n" + //
              "Proin eu ligula sit amet quam ornare porta mollis in nisl. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nullam eu mi sed orci luctus congue non sed urna. Maecenas viverra felis at lectus viverra, in pretium ante pulvinar. Quisque vel erat non nisi ullamcorper interdum. Sed sodales placerat metus, quis tincidunt nisi. Pellentesque dolor nisi, efficitur id nulla sed, pellentesque congue arcu. Maecenas vel dolor feugiat, tempor justo id, laoreet odio. Curabitur consequat finibus arcu a elementum. Donec a aliquet dolor. Maecenas tristique vitae magna sit amet efficitur. Aliquam lectus justo, consectetur non commodo non, vulputate eget sem. Aenean eu tellus in felis pharetra lacinia nec quis urna. Duis suscipit purus leo, eget bibendum purus finibus eget. Maecenas sed finibus neque. Integer quis ante nec augue finibus auctor.\n"
              + //
              "\n" + //
              "";

    final var encoded = sent.getBytes();

    final var packager = new Past<Object>(new MockTransport());
    final var sendConnection = packager.connect(new Object());
    //sendConnection.send(encoded);
    final var connection = packager.poll();
    final var payloads = connection.getPayloads();

    final String received = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(payloads.get(0))).toString();
    System.out.println(received.equals(sent));
    System.out.println(received);
    System.out.println("sent length: " + sent.length());
    System.out.println("received length: " + received.length());
  }

  private static class MockTransport implements ITransport<Object> {
    private static final short MTU = 1500;
    private final List<byte[]> packets = new ArrayList<>();

    private final Object clientEndpoint = new Object();

    @Override
    public short getSendPacketLengthLimit() {
      return MTU;
    }

    @Override
    public void send(byte[] packet, Object endpoint) {
      assert packet.length <= MTU;
      System.out.println("---> sending packet");
      packets.add(packet);
      final var hex = HexFormat.ofDelimiter(" ");
      final String hexString = hex.formatHex(packet);
      System.out.println(hexString);
    }

    @Override
    public TransportPacket<Object> receive() {
      System.out.println("---> receiving packet");
      return new TransportPacket<>(clientEndpoint, packets.remove(packets.size() - 1));
    }
  }
}
