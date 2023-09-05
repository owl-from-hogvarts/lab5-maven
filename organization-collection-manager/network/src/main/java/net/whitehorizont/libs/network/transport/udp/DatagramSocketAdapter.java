package net.whitehorizont.libs.network.transport.udp;

import net.whitehorizont.libs.network.past.ITransport;
import net.whitehorizont.libs.network.past.TransportPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class DatagramSocketAdapter implements ITransport<InetSocketAddress> {
    private static final short MTU = 1400;
    private DatagramSocket datagramSocket;

    public DatagramSocketAdapter(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public short getPacketLengthLimit() {
        return MTU;
    }

    @Override
    public void send(byte[] packet, InetSocketAddress endpoint) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, endpoint);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TransportPacket<InetSocketAddress> receive() {
        try {
            byte[] buffer = new byte[getPacketLengthLimit()];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            InetSocketAddress senderAddress = (InetSocketAddress) datagramPacket.getSocketAddress();
            byte[] data = Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength());
            return new TransportPacket<>(senderAddress, data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

