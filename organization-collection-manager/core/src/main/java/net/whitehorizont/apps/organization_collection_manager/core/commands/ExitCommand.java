package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public class ExitCommand implements ICommand<Void> {
  private CommandQueue commandQueue;
  // private @Nullable ICollectionManager<?, ?> collectionManager;

  public ExitCommand(CommandQueue commandQueue) {
    this.commandQueue = commandQueue;
  }

  @Override
  public Observable<Void> execute() {
    return commandQueue.terminate();
  }  
}
