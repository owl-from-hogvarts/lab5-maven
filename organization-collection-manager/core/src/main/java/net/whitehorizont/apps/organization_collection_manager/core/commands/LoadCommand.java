package net.whitehorizont.apps.organization_collection_manager.core.commands;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;

public class LoadCommand<E extends ICollectionElement<?>> implements ICommand<E> {

  @Override
  public Observable<@NonNull E> execute() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'execute'");
  }

  @Override
  public void setCollection(@NonNull Observable<@NonNull IBaseCollection<?, ?, ?>> collection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setCollection'");
  }

  @Override
  public boolean hasPreferredCollection() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'hasPreferredCollection'");
  }

  // create instance of FileStorage
  // place it into collection manager
  
}
