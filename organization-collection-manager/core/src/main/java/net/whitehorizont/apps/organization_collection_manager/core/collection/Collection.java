package net.whitehorizont.apps.organization_collection_manager.core.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata.Builder;

public class Collection<T, E> {

  private final Map<ElementId, E> elements = new LinkedHashMap<>();
  private final CollectionMetadata metadata;
  private final IDataSink<T> dataSink;

  public Collection(DataSinkSource<T, E> dataSink) {
    dataSink.subscribe(this::onNextElement);
    this.dataSink = dataSink;

    this.metadata = new CollectionMetadata(new Builder(new CollectionId()));

  }

  /**
   * Collection listens on returned sink to receive new elements
   */
  public IDataSink<T> getDataSink() {
    return dataSink;
  }

  // collection.getDataSink().supply(builder)
  // collection.getDataSink().onNext(builder) -- can be used as observer directly

  // Observable.just(builder).subscribe(collection.getDataSink())
  // Observable.just(builder).subscribe(collection.getDataSInk()::supply)
  // Observable.just(builder).toCollectionElement().subscribe(collection) --
  // collection may be subscribed to several sources witch is not permitted

  public Observable<E> getEvery$() {
    return Observable.just(elements).flatMap((elements) -> {
      var elementsList = new ArrayList<>(elements.values());

      return Observable.create(subscriber -> {

        for (var element : elementsList) {
          subscriber.onNext(element);
        }
      });
    });
  }

  public Observable<List<E>> getAll$() {
    return Observable.just(new ArrayList<>(elements.values()));
  }

  // stores creation time
  public CollectionMetadata getMetadataSnapshot() {
    return this.metadata;
  }

  public void clear() {
  }

  private void onNextElement(@NonNull E t) throws Throwable {

    // integrity should be checked during validation stage
    // when new elements arrives from data sink
    // // check it's integrity
    // // report if it is modified
    // if duplicate elements are encountered, warn a user
    // ask if he would like to remove or store duplicates
    // if user prefers to store duplicates, change the seed of element which arrived
    // later
    // if user agrees to proceed, update id and add element to collection
    this.elements.put(new ElementId(), t);
  }

}
