
package net.whitehorizont.libs.network.transport.udp.datagram_channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.libs.network.past.ITransport;
import net.whitehorizont.libs.network.past.TransportPacket;

@NonNullByDefault
public class DatagramChannelAdapter implements ITransport<InetSocketAddress> {
   private static final short MTU = 1400;
   
   private final DatagramChannel datagramChannel;

   public DatagramChannelAdapter(InetSocketAddress address) {
      try {
         this.datagramChannel = DatagramChannel.open();
         this.datagramChannel.bind(address);
      } catch (IOException ignore) {
         throw new RuntimeException();
      }
   }

   @Override
   public short getPacketLengthLimit() {
      return MTU;
   }

   @Override
   public void send(byte[] packet, InetSocketAddress endpoint) {
      try {
         ByteBuffer buffer = ByteBuffer.wrap(packet);
         datagramChannel.send(buffer, endpoint);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public TransportPacket<InetSocketAddress> receive() {
      try {
         ByteBuffer buffer = ByteBuffer.allocate(getPacketLengthLimit());
         InetSocketAddress senderAddress = (InetSocketAddress) datagramChannel.receive(buffer);
         byte[] data = Arrays.copyOf(buffer.array(), buffer.position());
         return new TransportPacket<>(senderAddress, data);
      } catch (IOException e) {
         throw new RuntimeException();
      }
   }
}

