package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.IWithId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

@NonNullByDefault
public interface ICollection<P extends IElementPrototype<?>, E extends IWithId<? extends BaseId>, M extends IWithId<? extends BaseId>> {

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
   */
  void replace(ISerializableKey key, P prototype) throws ValidationError;

  void delete(ISerializableKey key);

  Observable<E> getEvery$();

  Observable<Entry<ISerializableKey, E>> getEveryWithKey$();

  Observable<E> getById$(UUID_ElementId id);

  Observable<List<E>> getAll$();

  // stores creation time
  M getMetadataSnapshot();

  void clear();

  P getElementPrototype();

}