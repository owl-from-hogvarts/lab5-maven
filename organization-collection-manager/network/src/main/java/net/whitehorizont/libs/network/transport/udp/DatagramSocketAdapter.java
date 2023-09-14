package net.whitehorizont.libs.network.transport.udp;

import net.whitehorizont.libs.network.past.ITransport;
import net.whitehorizont.libs.network.past.TransportPacket;

import java.io.IOException;
import java.net.*;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DatagramSocketAdapter implements ITransport<InetSocketAddress> {
    private static final short MTU = 1400;
    private static final int MAX_PACKET_SIZE = 1<<16;
    private DatagramSocket datagramSocket;

    public DatagramSocketAdapter(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
    public void setTimeout(int timeout) {
        try {
            datagramSocket.setSoTimeout(timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    @Override
    public short getSendPacketLengthLimit() {
        return MTU;
    }

    @Override
    public void send(byte[] packet, InetSocketAddress endpoint) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, endpoint);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransportPacket<InetSocketAddress> receive() throws ServerTimeoutException {
        try {
            byte[] buffer = new byte[MAX_PACKET_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            InetSocketAddress senderAddress = (InetSocketAddress) datagramPacket.getSocketAddress();
            return new TransportPacket<InetSocketAddress>(senderAddress, buffer);
        } catch (SocketTimeoutException e) {
            throw new ServerTimeoutException("Receive operation timed out");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

