package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;

@NonNullByDefault
public class ShowCommand<E extends ICollectionElement<?>> implements ICommand<Entry<ISerializableKey, E>> {

  private final CollectionCommandReceiver<?, E, ?> collection;
  

  public ShowCommand(CollectionCommandReceiver<?, E, ?> collection) {
    this.collection = collection;
  }


  @Override
  public Observable<Entry<ISerializableKey, E>> execute() {
    return collection.getEveryWithKey$(); // who the fuck designed java generics
  }
  
}
