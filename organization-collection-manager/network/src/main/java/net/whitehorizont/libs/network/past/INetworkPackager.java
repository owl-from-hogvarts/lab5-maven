package net.whitehorizont.libs.network.past;

public interface INetworkPackager<Endpoint> {

  IConnection<Endpoint> send(Endpoint endpoint);

  IConnection<Endpoint> poll();

}