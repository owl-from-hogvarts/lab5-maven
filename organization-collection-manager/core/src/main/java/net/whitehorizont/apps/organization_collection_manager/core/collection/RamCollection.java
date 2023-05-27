package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_CollectionId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Holds collection in memory, without auto saving to disk
 */
@NonNullByDefault
public class RamCollection<P extends IElementPrototype<?>, E extends ICollectionElement<P>>
    implements ICollection<P, E, CollectionMetadata> {

  private final Map<ISerializableKey, E> elements = new LinkedHashMap<>();
  private final CollectionMetadata metadata;
  private final IElementFactory<P, E, ICollection<P, E, ?>, ?> elementFactory;

  // takes such dataSink factory which accepts any parent class of collection
  // we undertake to provide class not higher than collection
  public RamCollection(IElementFactory<P, E, ICollection<P, E, ?>, ?> elementFactory, CollectionMetadata metadata) {
    this.metadata = metadata;
    this.elementFactory = elementFactory;
  }

  public RamCollection(IElementFactory<P, E, ICollection<P, E, ?>, ?> elementFactory) {
    this(elementFactory, new CollectionMetadata(new Builder(new UUID_CollectionId())));
  }

  /**
   * Collection listens on returned sink to receive new elements
   * @throws ValidationError
   * @throws DuplicateElementsError
   */
  @Override
  public void insert(P prototype) throws ValidationError {
    insert(generateElementKey(), prototype);
  }

  private void insert(ISerializableKey key, P prototype) throws ValidationError {
    insert(key, this.elementFactory.buildElementFrom(prototype, this));
  }
  
  private void insert(ISerializableKey key, E element) throws ValidationError {
    this.elements.put(key, element);
  }

  @Override
  public Observable<E> getEvery$() {
    return Observable.just(elements).flatMap((elements) -> {
      @SuppressWarnings("null")
      final @NonNull var elementsList = elements.values();

      return Observable.fromIterable(elementsList);
    });
  }

  @Override
  public Observable<Entry<ISerializableKey, E>> getEveryWithKey$() {
    return Observable.just(elements).flatMap((elements) -> {
      @SuppressWarnings("null")
      final @NonNull Set<Entry<ISerializableKey, E>> keyValuePairs = elements.entrySet();

      return Observable.fromIterable(keyValuePairs);
    });
  }

  @Override
  public Observable<List<E>> getAll$() {
    return Observable.just(new ArrayList<>(elements.values()));
  }

  // stores creation time
  @Override
  public CollectionMetadata getMetadataSnapshot() {
    return this.metadata;
  }

  @Override
  public void clear() {
    this.elements.clear();
  }

  ElementKey generateElementKey() {
    return ElementKey.next();
  }

  @Override
  public void replace(ISerializableKey key, P prototype) throws ValidationError, NoSuchElement {
    checkIfExists(key);
    this.insert(key, prototype);
  }

  private void checkIfExists(ISerializableKey key) throws NoSuchElement {
    if (!this.elements.containsKey(key)) {
      throw new NoSuchElement(key);
    }
  }

  @Override
  public void delete(ISerializableKey key) throws NoSuchElement {
    checkIfExists(key);
    this.elements.remove(key);
  }

  @Override
  public P getElementPrototype() {
    return this.elementFactory.getElementPrototype();
  }

  @Override
  public BaseId getElementIdFromString(String idString) throws ValidationError {
    return this.elementFactory.getElementId(idString);
  }
}
