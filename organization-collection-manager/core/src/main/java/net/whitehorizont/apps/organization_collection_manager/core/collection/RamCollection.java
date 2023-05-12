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
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_CollectionId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

/**
 * Holds collection in memory, without auto saving to disk
 */
@NonNullByDefault
public class RamCollection<P extends IElementPrototype, E extends ICollectionElement<P>>
    implements ICollection<P, E, CollectionMetadata> {

  private final Map<ISerializableKey, E> elements = new LinkedHashMap<>();
  private final CollectionMetadata metadata;
  private final IElementFactory<P, E, ICollection<P, E, ?>> elementFactory;

  // takes such dataSink factory which accepts any parent class of collection
  // we undertake to provide class not higher than collection
  public RamCollection(IElementFactory<P, E, ICollection<P, E, ?>> elementFactory, CollectionMetadata metadata) {
    this.metadata = metadata;
    this.elementFactory = elementFactory;
  }

  public RamCollection(IElementFactory<P, E, ICollection<P, E, ?>> elementFactory) {
    this(elementFactory, new CollectionMetadata(new Builder(new UUID_CollectionId())));
  }

  /**
   * Collection listens on returned sink to receive new elements
   * @throws ValidationError
   */
  @Override
  public void insert(P prototype) throws ValidationError {
    this.elementFactory.buildElementFrom(prototype, this);
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
  public Observable<E> getById$(UUID_ElementId id) {
    return Observable.just(this.elements.get(id));
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

  private void onNextElement(E element) throws DuplicateElementsError {
    // integrity should be checked during validation stage
    // when new elements arrives from data sink
    // if duplicate elements are encountered, warn a user;
    // supplier must propose element with different id if user wants to store
    // duplicates
    // it is impossible to store two or more completely identical objects
    final var duplicate = elements.entrySet().stream().filter(entry -> entry.getValue().equals(element)).findFirst();
    if (duplicate.isPresent()) {
      // ask if he would like to remove or store duplicates
      throw new DuplicateElementsError(duplicate.get().getValue());
    }
    this.elements.put(element.getId(), element);
  }

  ElementKey generateElementKey(E element) {
    return ElementKey.next();
  }

  @Override
  public void replace(ISerializableKey key, P prototype) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'replace'");
  }

  @Override
  public void delete(ISerializableKey key) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}
