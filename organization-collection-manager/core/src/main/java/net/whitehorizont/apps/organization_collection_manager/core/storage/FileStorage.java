package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IBaseCollection;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

// provides adapter with file content
// adapters operate on collections
// treat data as opaque just write and read it
// composition and aggregation of data should be done somewhere else
// can store only one collection
public class FileStorage<C extends IBaseCollection<?, ?, M>, M>
    implements IBaseStorage<C, CollectionId, M> {
  private final IFileAdapter<C> adapter;
  private static final OpenOption[] DEFAULT_FILE_OPEN_OPTIONS = {
      StandardOpenOption.CREATE /* creates file if it does not exist */, StandardOpenOption.WRITE,
      StandardOpenOption.READ };
  // private ReplaySubject<C> collectionsLoaded = ReplaySubject.create();
  private final SeekableByteChannel fileChannel;

  public FileStorage(String path, IFileAdapter<C> adapter) throws StorageInaccessibleError {
    this.adapter = adapter;

    // create file object
    Path file = PathHelpers.preparePath(Paths.get(path));
    // verify that file exists
    if (!Files.exists(file)) {
      // create all missing parent directories
      // if could not create parent dirs, report what when wrong (perm)

      PathHelpers.createParentDirectories(file);
    }

    try (var channel = Files.newByteChannel(file, FileStorage.DEFAULT_FILE_OPEN_OPTIONS)) {
      fileChannel = channel;
    } catch (IOException e) {
      throw new StorageInaccessibleError();
    }
  }


  @Override
  public void save(@NonNull C t) {
    // receive collection to store
    // use adaptor to serialize collection
    // adapter may decide not to serialize collection
    // in that case it returns null or throws error

    final ByteBuffer fileContent = this.adapter.serialize(t);
    try {
      fileChannel.write(fileContent);
    } catch (IOException e) {
    }
    

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
  public Observable<C> load() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'load'");
  }


  @Override
  public Observable<C> load(CollectionId key) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'load'");
  }


  @Override
  public Observable<C> loadAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadAll'");
  }


  @Override
  public Observable<M> loadMetadata() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadMetadata'");
  }


  @Override
  public Observable<M> loadMetadata(CollectionId key) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadMetadata'");
  }

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
