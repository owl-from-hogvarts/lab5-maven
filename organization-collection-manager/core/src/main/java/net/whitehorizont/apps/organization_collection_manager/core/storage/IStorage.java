package net.whitehorizont.apps.organization_collection_manager.core.storage;

import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;

// Provides abstraction over source of collections
// order of collections is not guaranteed
// storage can report interactive warnings and errors

public interface IStorage extends ObservableOnSubscribe, Observer {
  
}
