package net.whitehorizont.apps.collection_manager.core.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class ShowCommand<C extends ICollectionCommandReceiver<E>, E extends ICollectionElement<E>> implements ICommand<Entry<ElementKey, E>, IProvideCollectionReceiver<C>> {
  @Override
  public Observable<Entry<ElementKey, E>> execute(IProvideCollectionReceiver<C> dependencyProvider) {
    return dependencyProvider.getCollectionReceiver().getEveryWithKey$(); // who the fuck designed java generics
  }
  
}
