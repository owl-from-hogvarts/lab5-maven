package net.whitehorizont.apps.organization_collection_manager.core.collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.whitehorizont.apps.organization_collection_manager.lib.ValidationError;

// the goal of class is to transform Builder of collection element into element of collection
/** Prepare data to be added to collection by transforming it into collection elements
 * 
 * @param <P> what is supplied, prepared and transformed into collection element
 * @param <E> type of collection element
 */
@NonNullByDefault
public abstract class DataSinkSource<P extends IElementPrototype, E, V> extends Observable<@NonNull E> implements IDataSink<P> {
  private Subject<E> elements = PublishSubject.<E>create();
  private final V validationObject;
  
  public DataSinkSource(V validationObject) {
    this.validationObject = validationObject;
  }

  final protected V getValidationObject() {
    return validationObject;
  }

  final public void supply(P prototype) throws ValidationError {
    supply(prototype, false);
  }
  final public void supply(P prototype, boolean force) throws ValidationError {
    final var element = buildElementFrom(prototype);
    elements.onNext(element);
  }

  abstract protected @NonNull E buildElementFrom(P prototype) throws ValidationError; 

  @Override
  protected final void subscribeActual(Observer<? super E> observer) {
    elements.subscribe(observer);
  }

}
