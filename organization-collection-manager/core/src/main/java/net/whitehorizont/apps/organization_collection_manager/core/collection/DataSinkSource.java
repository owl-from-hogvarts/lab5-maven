package net.whitehorizont.apps.organization_collection_manager.core.collection;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

// the goal of class is to transform Builder of collection element into element of collection
/** Prepare data to be added to collection by transforming it into collection elements
 * 
 * @param <T> what is supplied, prepared and transformed into collection element
 * @param <E> type of collection element
 */
public abstract class DataSinkSource<T, E> extends Observable<E> implements IDataSink<T> {
  private Subject<E> elements = PublishSubject.<E>create();
  
  abstract public void supply(T prototype);

  @Override
  protected final void subscribeActual(@NonNull Observer<? super @NonNull E> observer) {
    elements.subscribe(observer);
  }

}
