package net.whitehorizont.libs.network.past;

public interface INetworkPackager<Endpoint> {

  Connection<Endpoint> send(Endpoint endpoint);

  Connection<Endpoint> poll();

}