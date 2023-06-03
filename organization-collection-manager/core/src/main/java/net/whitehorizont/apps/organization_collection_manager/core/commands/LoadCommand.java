package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;

@NonNullByDefault
public class LoadCommand<E extends ICollectionElement<?>> implements ICommand<E> {

  private final CollectionCommandReceiver<?, E> receiver;


  public LoadCommand(CollectionCommandReceiver<?, E> receiver) {
    this.receiver = receiver;
  }


  @Override
  public Observable<@NonNull E> execute() {
    return receiver.getEvery$();

  }
  // create instance of FileStorage
  // place it into collection manager
  
}
