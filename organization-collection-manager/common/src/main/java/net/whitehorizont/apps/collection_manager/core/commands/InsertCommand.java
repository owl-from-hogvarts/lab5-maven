package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class InsertCommand<C extends ICollectionCommandReceiver<E>, E extends ICollectionElement<E>> implements ICommand<Void, IProvideCollectionReceiver<C>> {
  private final E element;
  private final String key;

  public InsertCommand(String key, E element) {
    this.element = element;
    this.key = key;
  }

  @Override
  public Observable<Void> execute(IProvideCollectionReceiver<C> dependencyProvider) {
    final C collection = dependencyProvider.getCollectionReceiver();
    
    return Observable.create(subscriber -> {
      collection.insert(key, element);
      subscriber.onComplete();
    });
  }
  
}
