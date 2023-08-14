package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;

@NonNullByDefault
public interface ICollectionManagerReceiver<C extends ICollection<?>> extends ICollectionManager<C> {

  Observable<C> getCollection() throws Exception;

  void save(BaseId collectionId) throws Exception;

}