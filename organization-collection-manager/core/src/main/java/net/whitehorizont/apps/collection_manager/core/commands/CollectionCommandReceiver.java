package net.whitehorizont.apps.collection_manager.core.commands;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Trivial command receiver.
 * It does absolutely nothing except just passing calls to underling collection
 */
@NonNullByDefault
public class CollectionCommandReceiver<E extends ICollectionElement<E>> implements ICollectionCommandReceiver<E> {
  protected final ICollection<E> collection;

  public CollectionCommandReceiver(ICollection<E> collection) {
    this.collection = collection;
  }


  @Override
  public void replace(ElementKey key, E element) throws ValidationError, NoSuchElement, StorageInaccessibleError {
    this.collection.replace(key, element);
  }

  @Override
  public E delete(ElementKey key) throws NoSuchElement, ValidationError, StorageInaccessibleError {
    return this.collection.delete(key);
  }

  @Override
  public Observable<E> getEvery$() {
    return this.collection.getEvery$();
  }

  @Override
  public Observable<Pair<ElementKey, E>> getEveryWithKey$() {
    return this.collection.getEveryWithKey$();
  }

  @Override
  public Observable<List<E>> getAll$() {
    return this.collection.getAll$();
  }

  @Override
  public CollectionMetadata getPersistentMetadata() {
    return this.collection.getPersistentMetadata();
  }

  @Override
  public CollectionMetadataComputed getMetadataTree() {
    // create
    final CollectionMetadata persistentMetadata = this.collection.getPersistentMetadata();
    final CollectionMetadataComputed metadata = persistentMetadata.createWritable();

    // modify
    final long elementCount = this.collection.getEvery$().count().blockingGet();
    metadata.setElementCount((int) elementCount);

    metadata.setCollectionType(getCollectionType());

    // return
    return metadata;
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
  public void insert(E element) throws ValidationError, DuplicateElements, KeyGenerationError, StorageInaccessibleError {
    this.collection.insert(element);
  }


  @Override
  public void insert(ElementKey key, E element) throws ValidationError, DuplicateElements, StorageInaccessibleError {
    this.collection.insert(key, element);
  }

  public void insert(String key, E element) throws ValidationError, DuplicateElements, StorageInaccessibleError {
    final ElementKey keyParsed = collection.getElementKeyFromString(key);
    this.collection.insert(keyParsed, element);
  }
  
}
