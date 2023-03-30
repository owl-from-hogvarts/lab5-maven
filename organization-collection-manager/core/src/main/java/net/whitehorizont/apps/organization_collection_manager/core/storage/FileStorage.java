package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

// provides adapter with file content
// adapters operate on collections
public class FileStorage<E, M, C extends IStorableKeyedCollection<E, M>> extends Observable<C> implements IStorage<C> {
  private IFileAdapter<E, M> adapter;
  
  public FileStorage(String path, IFileAdapter<E, M> adapter) throws StorageInaccessibleError {
    this.adapter = adapter;
    
    // create file object
    Path file = PathHelpers.preparePath(Paths.get(path));
    // verify that file exists
    // if file does not exist, create it
    if (!Files.exists(file)) {
      // create all missing parent directories
      // if could not create parent dirs, report what when wrong (perm)
      PathHelpers.createParentDirectories(file);
    }
    // may be just use iterators
    // 
    // if file exists
      // create observable
      // apply delayWhen(ready)
      // emit two observables and complete
      // one observable represents warnings
      // the other one represents collections
      // verify integrity
      // check if hash sum stored in file match the computed one
    // error if data was modified (integrity check failed) by external factors
    // report that resource is available
  }

  public void load() {
    // if no collection id specified, consider default or all collections

    
  }

  @Override
  public void onSubscribe(@NonNull Disposable d) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onSubscribe'");
  }

  @Override
  public void onNext(@NonNull C t) {
    // receive collection to store
    // use adaptor to serialize collections
    this.adapter.serialize(t);
    // accumulate file content
    // compute file metadata using adapter
    // check if file is still available
    // if file is not available, do the exact same thing as in constructor
    // if everything ok, check if enough space is available for writing
    // write down serialized data
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onNext'");
  }

  @Override
  public void onError(@NonNull Throwable e) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onError'");
  }

  @Override
  public void onComplete() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onComplete'");
  }

  @Override
  protected void subscribeActual(@NonNull Observer<? super @NonNull C> observer) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'subscribeActual'");
  }
}
