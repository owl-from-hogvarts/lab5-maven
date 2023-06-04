package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.time.Instant;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class InfoCommand implements ICommand<Instant> {

  private final CollectionCommandReceiver<?, ?> collection;

  public InfoCommand(CollectionCommandReceiver<?, ?> collection) {
    this.collection = collection;
  }

  @Override
  public Observable<Instant> execute() {
    return Observable.just(collection.getMetadataSnapshot().getCreationTime());
  }
  
}
