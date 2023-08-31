package net.whitehorizont.libs.network.past;

public interface INetworkPackager<Endpoint> {

  IConnection<Endpoint> connect(Endpoint endpoint);

  IConnection<Endpoint> poll();

}