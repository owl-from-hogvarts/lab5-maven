package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideCollectionReceiver;

@NonNullByDefault
public class ClearCommand implements ICommand<Void, IProvideCollectionReceiver<?>> {

  @Override
  public Observable<Void> execute(IProvideCollectionReceiver<?> dependencyProvider) {
    return Observable.create(subscriber -> {
      dependencyProvider.getCollectionReceiver().clear();
      subscriber.onComplete();
    });
  }
}
