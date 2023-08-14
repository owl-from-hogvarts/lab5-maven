package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class LoadCommand<C extends ICollectionCommandReceiver<E>, E extends ICollectionElement<E>> implements ICommand<E, IProvideCollectionReceiver<C>> {

  @Override
  public Observable<E> execute(IProvideCollectionReceiver<C> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().getEvery$();
  }
  
}
