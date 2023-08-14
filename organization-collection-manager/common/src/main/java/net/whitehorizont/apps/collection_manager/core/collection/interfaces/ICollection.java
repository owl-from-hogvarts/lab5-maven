package net.whitehorizont.apps.collection_manager.core.collection.interfaces;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
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
   * @throws NoSuchElement
   */
  void insert(E element) throws Exception;
  void insert(ElementKey key, E element) throws Exception;

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
   */
  void replace(ElementKey key, E element) throws ValidationError, NoSuchElement;

  E delete(ElementKey key) throws NoSuchElement;

  Observable<E> getEvery$();

  Observable<Entry<ElementKey, E>> getEveryWithKey$();

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