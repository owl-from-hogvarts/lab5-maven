package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;

@NonNullByDefault
public class ShowCommand<T extends ICollectionElement<?>> implements ICommand<T> {

  private final CollectionCommandReceiver<?, T, ?> collection;
  

  public ShowCommand(CollectionCommandReceiver<?, T, ?> collection) {
    this.collection = collection;
  }


  @Override
  public Observable<T> execute() {
    return collection.getEvery$(); // who the fuck designed java generics
  }
  
}
