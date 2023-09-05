package net.whitehorizont.apps.collection_manager.core.collection.keys;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ISerializableKey extends Serializable {
  String serialize();
}
