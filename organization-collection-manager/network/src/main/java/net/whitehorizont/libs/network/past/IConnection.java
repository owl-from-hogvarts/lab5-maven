package net.whitehorizont.libs.network.past;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IConnection<Endpoint> {

  // WHOLE PACKAGE AT ONCE
  void send(byte[] payload);

  // Immediately returns packages which are completely received
  List<byte[]> getPayloads();

  // Awaits until at least one package is completely received
  List<byte[]> await();

  Endpoint getEndpoint();
}