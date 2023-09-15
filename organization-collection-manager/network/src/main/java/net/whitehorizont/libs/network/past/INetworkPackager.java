package net.whitehorizont.libs.network.past;

import net.whitehorizont.libs.network.transport.udp.ReceiveTimeoutException;

public interface INetworkPackager<Endpoint> {

  IConnection<Endpoint> connect(Endpoint endpoint);

  IConnection<Endpoint> poll() throws ReceiveTimeoutException;

}