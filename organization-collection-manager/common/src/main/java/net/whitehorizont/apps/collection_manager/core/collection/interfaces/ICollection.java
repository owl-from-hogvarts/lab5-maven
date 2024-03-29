package net.whitehorizont.apps.collection_manager.core.collection.interfaces;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Ideally, implementations should register validators and validate elements!
 * If there is any way to enforce validators, implement it pls 😉
 */
@NonNullByDefault
public interface ICollection<E extends ICollectionElement<E>> {

  /**
   * Collection listens on returned sink to receive new elements
   * Multiple elements can be passed
   * 
   * @throws ValidationError
   * @throws DuplicateElements
   * @throws KeyGenerationError
   * @throws StorageInaccessibleError
   * @throws NoSuchElement
   */
  void insert(E element) throws ValidationError, DuplicateElements, KeyGenerationError, StorageInaccessibleError;
  void insert(ElementKey key, E element) throws ValidationError, DuplicateElements, StorageInaccessibleError;

  /**
   * ! Only single element should be supplied !
   * 
   * In dev build passing more elements should result in assertion error
   * at runtime other elements should be ignored
   * 
   * @param key
   * @return
   * @throws ValidationError
   * @throws NoSuchElement
   * @throws StorageInaccessibleError
   */
  void replace(ElementKey key, E element) throws ValidationError, NoSuchElement, StorageInaccessibleError;

  E delete(ElementKey key) throws NoSuchElement, ValidationError, StorageInaccessibleError;

  Observable<E> getEvery$();

  Observable<Pair<ElementKey, E>> getEveryWithKey$();

  Observable<List<E>> getAll$();

  // stores creation time
  CollectionMetadata getPersistentMetadata();

  void clear();

  String getCollectionType();

  /**
   * Try to not mess up with element ids! Ids are the part of element. Keys are a
   * part of collection and more of implementation detail
   */
  // DONE: ideally keys should be separated from ids by type hierarchy
  ElementKey getElementKeyFromString(String keyString) throws ValidationError;

}