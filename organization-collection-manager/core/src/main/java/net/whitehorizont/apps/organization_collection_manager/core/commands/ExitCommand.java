package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.libs.file_system.AssertHelpers;

@NonNullByDefault
public class ExitCommand implements ISystemCommand<Void> {
  private @Nullable CommandQueue commandQueue;
  // private @Nullable ICollectionManager<?, ?> collectionManager;

  @Override
  public Observable<Void> execute() {
    assert commandQueue != null : AssertHelpers.getAssertMessageFor("ExitCommand.java", "execute");
    return commandQueue.terminate();
  }

  @Override
  public void setCommandQueue(CommandQueue queue) {
    this.commandQueue = queue;
  }

  @Override
  public void setCollectionManager(ICollectionManager<?, ?> collectionManager) {
  }
  
}
