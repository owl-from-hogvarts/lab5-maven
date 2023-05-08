package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata.Builder;

// collection defines id type to use. adapter is responsible for properly serializing it
@NonNullByDefault
public class Collection<P, E extends ICollectionElement<P, ? extends BaseId>>
    implements IBaseCollection<P, E, CollectionMetadata> {

  private final Map<ISerializableKey, E> elements = new LinkedHashMap<>();
  private final CollectionMetadata metadata;
  private final IDataSink<P> dataSink;

  // takes such dataSink factory which accepts any parent class of collection
  public Collection(IDataSinkSourceFactory<P, E, ? super Collection<P, E>> dataSinkFactory, CollectionMetadata metadata) {
    this.metadata = metadata;

    final var dataSink = dataSinkFactory.getDataSinkSourceFor(this);
    this.dataSink = dataSink;
    dataSink.subscribe(this::onNextElement);
  }

  public Collection(IDataSinkSourceFactory<P, E, Collection<P, E>> dataSinkFactory) {
    this(dataSinkFactory, new CollectionMetadata(new Builder(new UUID_CollectionId())));
  }

  /**
   * Collection listens on returned sink to receive new elements
   */
  @Override
  public IDataSink<P> getDataSink() {
    return dataSink;
  }

  @Override
  public @NonNull Observable<E> getEvery$() {
    return Observable.just(elements).flatMap((elements) -> {
      final var elementsList = elements.values();

      return Observable.fromIterable(elementsList);
    });
  }

  @Override
  public @NonNull Observable<Entry<ISerializableKey, E>> getEveryWithKey$() {
    return Observable.just(elements).flatMap((elements) -> {
      final Set<Entry<ISerializableKey, E>> keyValuePairs = elements.entrySet();

      return Observable.fromIterable(keyValuePairs);
    });
  }

  @Override
  public Observable<E> getById$(UUID_ElementId id) {
    return Observable.just(this.elements.get(id));
  }

  @Override
  public @NonNull Observable<List<E>> getAll$() {
    return Observable.just(new ArrayList<>(elements.values()));
  }

  // stores creation time
  @Override
  public @NonNull CollectionMetadata getMetadataSnapshot() {
    return this.metadata;
  }

  @Override
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

  void generateElementKey(E element) {
    // TODO: implement
  }

}
