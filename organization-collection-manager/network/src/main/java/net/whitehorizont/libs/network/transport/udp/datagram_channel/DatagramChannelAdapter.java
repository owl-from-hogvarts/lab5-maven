
package net.whitehorizont.libs.network.transport.udp.datagram_channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.libs.network.past.ITransport;
import net.whitehorizont.libs.network.past.TransportPacket;
import net.whitehorizont.libs.network.transport.udp.ReceiveTimeoutException;

@NonNullByDefault
public class DatagramChannelAdapter implements ITransport<InetSocketAddress> {
   private static final short MTU = 1400;
   private static final int MAX_PACKET_SIZE = 1<<16;
   private static final long RECEIVE_INTERVAL_MS = 100;
   
   private final DatagramChannel datagramChannel;
   private long timeoutMs = 0;

   public DatagramChannelAdapter(InetSocketAddress address) {
      try {
         this.datagramChannel = DatagramChannel.open();
         this.datagramChannel.bind(address);
         this.datagramChannel.configureBlocking(false);
      } catch (IOException ignore) {
         throw new RuntimeException();
      }
   }

   @Override
   public short getSendPacketLengthLimit() {
      return MTU;
   }

   @Override
   public void send(byte[] packet, InetSocketAddress endpoint) {
      try {
         ByteBuffer buffer = ByteBuffer.wrap(packet);
         datagramChannel.send(buffer, endpoint);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public TransportPacket<InetSocketAddress> receive() throws ReceiveTimeoutException {
      final long time = System.currentTimeMillis();

      while (true) {
         InetSocketAddress senderAddress = null;
         try {
            final ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            senderAddress = (InetSocketAddress) datagramChannel.receive(buffer);
            if (senderAddress != null) {
               System.out.println("Data received. Sender address " + senderAddress);
               return new TransportPacket<>(senderAddress, buffer.array());
            }
            Thread.sleep(RECEIVE_INTERVAL_MS);
         } catch (IOException e) {
            throw new RuntimeException(e);
         } catch (InterruptedException ignore) {
            // time between calls to datagramChannel does not matter that much
         }

         // out of try statement, because should be executed for ignored exceptions too
         if (((System.currentTimeMillis() - time) > this.timeoutMs) && senderAddress == null) {
            throw new ReceiveTimeoutException("No activity on the line");
         }
      }
   }

   @Override
   public void setTimeout(long timeoutMs) {
      this.timeoutMs = timeoutMs;
   }
}

