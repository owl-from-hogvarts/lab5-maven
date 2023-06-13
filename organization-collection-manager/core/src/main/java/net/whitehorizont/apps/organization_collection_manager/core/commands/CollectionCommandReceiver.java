package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.DuplicateElements;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.NoSuchElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.organization_collection_manager.lib.BasicFieldMetadata;
import net.whitehorizont.apps.organization_collection_manager.lib.ReadonlyField;
import net.whitehorizont.apps.organization_collection_manager.lib.TitledNode;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Trivial command receiver.
 * It does absolutely nothing except just passing calls to underling collection
 */
@NonNullByDefault
public class CollectionCommandReceiver<E extends ICollectionElement> implements ICollection<E> {
  protected final ICollection<E> collection;

  public CollectionCommandReceiver(ICollection<E> collection) {
    this.collection = collection;
  }


  @Override
  public void replace(ElementKey key, E element) throws ValidationError, NoSuchElement {
    this.collection.replace(key, element);
  }

  @Override
  public E delete(ElementKey key) throws NoSuchElement {
    return this.collection.delete(key);
  }

  @Override
  public Observable<E> getEvery$() {
    return this.collection.getEvery$();
  }

  @Override
  public Observable<Entry<ElementKey, E>> getEveryWithKey$() {
    return this.collection.getEveryWithKey$();
  }

  @Override
  public Observable<List<E>> getAll$() {
    return this.collection.getAll$();
  }

  @Override
  public CollectionMetadata getMetadataSnapshot() {
    return this.collection.getMetadataSnapshot();
  }

  public TitledNode<?, ReadonlyField<?>> getMetadataTree() {
    final TitledNode<?, ReadonlyField<?>> metadataTree = this.collection.getMetadataSnapshot().getTree();
    final var metadataTopLevelFields = new ArrayList<>(metadataTree.getLeafs());

    final var countElements = this.collection.getEvery$().count().blockingGet();
    metadataTopLevelFields.add(new ReadonlyField<>(new BasicFieldMetadata("Elements count"), countElements));
    final var collectionType = this.collection.getCollectionType();
    // DONE: request collection type from factory
    metadataTopLevelFields.add(new ReadonlyField<>(new BasicFieldMetadata("Type of collection"), collectionType));
    metadataTopLevelFields.add(new ReadonlyField<>(new BasicFieldMetadata("Etc. "), "etc."));

    return new TitledNode<>(metadataTree.getDisplayedName(), metadataTopLevelFields, metadataTree.getChildren());
  }

  @Override
  public void clear() {
    this.collection.clear();
  }


  @Override
  public ElementKey getElementKeyFromString(String keyString) throws ValidationError {
    return this.collection.getElementKeyFromString(keyString);
  }

  @Override
  public String getCollectionType() {
    return this.collection.getCollectionType();
  }


  @Override
  public void insert(E element) throws ValidationError, DuplicateElements, KeyGenerationError {
    this.collection.insert(element);
  }


  @Override
  public void insert(ElementKey key, E element) throws ValidationError, DuplicateElements {
    this.collection.insert(key, element);
  }
  
}
