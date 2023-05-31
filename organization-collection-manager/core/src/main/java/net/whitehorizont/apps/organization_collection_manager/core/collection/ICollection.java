package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface ICollection<P extends IElementPrototype<?>, E extends ICollectionElement<P>, M extends IWithId<? extends BaseId>> {

  /**
   * Collection listens on returned sink to receive new elements
   * Multiple elements can be passed
   * 
   * @throws ValidationError
   * @throws DuplicateElements
   * @throws NoSuchElement
   */
  void insert(P rawElementData) throws ValidationError, DuplicateElements;
  void insert(BaseId key, P prototype) throws ValidationError, DuplicateElements;

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
  void replace(BaseId key, P prototype) throws ValidationError, NoSuchElement;

  E delete(BaseId key) throws NoSuchElement;

  Observable<E> getEvery$();

  Observable<Entry<BaseId, E>> getEveryWithKey$();

  Observable<List<E>> getAll$();

  // stores creation time
  M getMetadataSnapshot();

  void clear();

  P getElementPrototype();

  BaseId getElementIdFromString(String idString) throws ValidationError;

  /**
   * Try to not mess up with element ids! Ids are the part of element. Keys are a
   * part of collection and more of implementation detail
   */
  // TODO: ideally keys should be separated from ids by type hierarchy
  BaseId getElementKeyFromString(String keyString) throws ValidationError;

}