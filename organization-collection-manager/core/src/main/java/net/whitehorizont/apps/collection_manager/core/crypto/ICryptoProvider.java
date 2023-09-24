package net.whitehorizont.apps.collection_manager.core.crypto;

import net.whitehorizont.apps.organization_collection_manager.lib.IDisplayable;

public interface ICryptoProvider extends IDisplayable {
  public byte[] apply(byte[] data);
  /**
   * Length of hash in bytes
   * @return
   */
  public int getHashLength();
}
