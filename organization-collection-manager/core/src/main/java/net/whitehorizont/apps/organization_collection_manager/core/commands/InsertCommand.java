package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;

@NonNullByDefault
public class InsertCommand<P, C extends IBaseCollection<P, ?, ?>> extends BaseCommand<Void, C> {
  private final P prototype;

  public InsertCommand(P prototype) {
    // receive collection prototype
    // store it
    this.prototype = prototype;
  }

  // @Override
  public Observable<Void> execute() {
    return Observable.create(subscriber -> {
      final var collection = getCollection();
      assert collection != null;
      collection.getDataSink().supply(prototype);
    });
  }
  
}
