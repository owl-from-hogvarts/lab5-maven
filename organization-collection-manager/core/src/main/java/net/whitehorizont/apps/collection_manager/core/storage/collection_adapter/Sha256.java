package net.whitehorizont.apps.collection_manager.core.storage.collection_adapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.rxjava3.annotations.NonNull;

public class Sha256 implements IntegrityAlgorithm {
  private static final String ALGORITHM_NAME = "SHA-256";
  private static MessageDigest encoder;
  static {
    try {
      encoder = MessageDigest.getInstance(ALGORITHM_NAME);
    } catch(NoSuchAlgorithmException e) {
    }
  }


  @Override
  public @NonNull String getDisplayedName() {
    return Sha256.ALGORITHM_NAME;
  }

  @Override
  public byte[] apply(byte[] data) {
    return encoder.digest(data);
  }
  
}
