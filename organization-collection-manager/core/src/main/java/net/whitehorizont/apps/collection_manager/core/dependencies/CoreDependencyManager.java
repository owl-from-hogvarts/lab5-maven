package net.whitehorizont.apps.collection_manager.core.dependencies;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.commands.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.ICollectionManagerReceiver;

@NonNullByDefault
public class CoreDependencyManager<CR extends ICollectionCommandReceiver<E>, E extends ICollectionElement<E>> implements IProvideCollectionReceiver<CR>,IProvideNothing, IProvideCollectionManagerReceiver<ICollection<E>> {
  final CR collectionReceiver;
  final ICollectionManagerReceiver<ICollection<E>> collectionManagerReceiver;
  

  public CoreDependencyManager(CR collectionReceiver, ICollectionManagerReceiver<ICollection<E>> collectionManagerReceiver) {
    this.collectionReceiver = collectionReceiver;
    this.collectionManagerReceiver = collectionManagerReceiver;
  }

  @Override
  public ICollectionManagerReceiver<ICollection<E>> getCollectionManagerReceiver() {
    return collectionManagerReceiver;
  }

  @Override
  public CR getCollectionReceiver() {
    return collectionReceiver;
  }
  
}
