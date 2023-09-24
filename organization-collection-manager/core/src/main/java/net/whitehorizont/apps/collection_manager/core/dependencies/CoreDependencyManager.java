package net.whitehorizont.apps.collection_manager.core.dependencies;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IAuthReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionManagerReceiver;

@NonNullByDefault
public class CoreDependencyManager<CR extends ICollectionCommandReceiver<E>, E extends ICollectionElement<E>> implements IUniversalCoreProvider<CR, E> {
  private final CR collectionReceiver;
  private final ICollectionManagerReceiver<ICollection<E>> collectionManagerReceiver;
  private final IAuthReceiver authReceiver;
  

  public CoreDependencyManager(CR collectionReceiver, ICollectionManagerReceiver<ICollection<E>> collectionManagerReceiver, IAuthReceiver authReceiver) {
    this.collectionReceiver = collectionReceiver;
    this.collectionManagerReceiver = collectionManagerReceiver;
    this.authReceiver = authReceiver;
  }

  @Override
  public ICollectionManagerReceiver<ICollection<E>> getCollectionManagerReceiver() {
    return collectionManagerReceiver;
  }

  @Override
  public CR getCollectionReceiver() {
    return collectionReceiver;
  }

  @Override
  public IAuthReceiver getAuthReceiver() {
    return authReceiver;
  }
  
}
