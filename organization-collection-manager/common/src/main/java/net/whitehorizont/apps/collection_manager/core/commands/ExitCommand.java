package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class ExitCommand implements ICommand<Void> {
  @Override
  public Observable<Void> execute() {
    System.exit(0);
    return Observable.empty();
  }  
}
