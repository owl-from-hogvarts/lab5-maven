package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.storage.IStorableKeyedCollection;

// collection defines id type to use. adapter is responsible to properly serialize it
public class Collection<T, E extends IWithId<? extends ISerializableKey>>
    implements IStorableKeyedCollection<E, CollectionMetadata> {

  private final Map<ISerializableKey, E> elements = new LinkedHashMap<>();
  private final CollectionMetadata metadata;
  private final IDataSink<T> dataSink;

  public Collection(DataSinkSource<T, E, Collection<T, E>> dataSink, CollectionMetadata metadata) {
    dataSink.subscribe(this::onNextElement);
    this.dataSink = dataSink;
    this.metadata = metadata;
  }

  public Collection(DataSinkSource<T, E, Collection<T, E>> dataSink) {
    this(dataSink, new CollectionMetadata(new Builder(new CollectionId())));
  }

  /**
   * Collection listens on returned sink to receive new elements
   */
  public IDataSink<T> getDataSink() {
    return dataSink;
  }

  public Observable<E> getEvery$() {
    return Observable.just(elements).flatMap((elements) -> {
      var elementsList = elements.values();

      return Observable.fromIterable(elementsList);
    });
  }

  // @Override
  // @SuppressWarnings("unchecked") // FUCK JAVA!!!
  public Observable<Entry<ISerializableKey, E>> getEveryWithKey$() {
    return Observable.just(elements).flatMap((elements) -> {
      final Set<Entry<ISerializableKey, E>> keyValuePairs = elements.entrySet();

      return Observable.fromIterable(keyValuePairs);
    });
  }

  public Observable<E> getById$(ElementId id) {
    return Observable.just(this.elements.get(id));
  }

  public Observable<List<E>> getAll$() {
    return Observable.just(new ArrayList<>(elements.values()));
  }

  // stores creation time
  public CollectionMetadata getMetadataSnapshot() {
    return this.metadata;
  }

  public void clear() {
    this.elements.clear();
  }

  private void onNextElement(@NonNull E element) throws DuplicateElementsError {
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

}
