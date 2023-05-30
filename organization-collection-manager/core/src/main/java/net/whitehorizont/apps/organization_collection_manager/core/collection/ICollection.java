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
   * @throws ValidationError
   */
  void insert(P rawElementData) throws ValidationError;
  /**
   * ! Only single element should be supplied !
   * 
   * In dev build passing more elements should result in assertion error
   * at runtime other elements should be ignored
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

}