package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public interface IAuthReceiver {
  void register(String login, String password) throws StorageInaccessibleError;
  Observable<Void> login(String login, String password);
}
