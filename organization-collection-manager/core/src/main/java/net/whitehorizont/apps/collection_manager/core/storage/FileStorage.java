package net.whitehorizont.apps.collection_manager.core.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.storage.errors.CollectionNotFound;
import net.whitehorizont.apps.collection_manager.core.storage.errors.ResourceEmpty;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.core.storage.errors.TooLargeFile;
import net.whitehorizont.libs.file_system.PathHelpers;

// provides adapter with file content
// adapters operate on collections
// treat data as opaque just write and read it
// composition and aggregation of data should be done somewhere else
// can store only one collection
@NonNullByDefault
public class FileStorage<C extends ICollection<?>>
    implements IBaseStorage<C> {
  private final IFileAdapter<C> adapter;
  private final Path path;

  public IFileAdapter<C> getAdapter() {
    return adapter;
  }

  private static final OpenOption[] DEFAULT_FILE_OPEN_OPTIONS = {
      StandardOpenOption.CREATE /* creates file if it does not exist */, 
      StandardOpenOption.WRITE,
      StandardOpenOption.READ };

  public FileStorage(String path, IFileAdapter<C> adapter) {
    this.adapter = adapter;
    @SuppressWarnings("null")
    final @NonNull Path preparedPath = PathHelpers.resolve(Paths.get(path));
    this.path = preparedPath;
  }

  @Override
  public void save(C collection) throws StorageInaccessibleError {
    // receive collection to store
    // use adaptor to serialize collection
    // adapter may decide not to serialize collection
    // in that case it returns null or throws error
    // ? supply content of file to adapter so it can enrich it
    // ? and not just override
    final byte[] fileContent = this.getAdapter().serialize(collection);
    try (final var fileChannel = getFileChannel()) {
      fileChannel.truncate(fileContent.length);
      fileChannel.write(ByteBuffer.wrap(fileContent));
    } catch (IOException e) {
      throw new StorageInaccessibleError();
    }
  }

  @Override
  public Observable<C> load() {
    // read file
    // receive collection
    return Observable.create((subscriber) -> {
      try {
        final var fileContent = readFile();

        try {
          @SuppressWarnings("null")
          final byte @NonNull[] fileContentBytes = fileContent.array();
          subscriber.onNext(this.getAdapter().deserialize(fileContentBytes));
          subscriber.onComplete();
        } catch (ResourceEmpty e) {
          subscriber.onNext(this.getAdapter().deserializeSafe());
          subscriber.onComplete();
        }

      } catch (IOException e) {
        subscriber.onError(e);
      }
    });
  }

  private ByteBuffer readFile() throws StorageInaccessibleError, TooLargeFile {

    try (final var channel = getFileChannel()) {
      final var fullFileSize = Files.size(path);
      final int fileSizeTruncated = (int) fullFileSize;
      if (fullFileSize != (long) fileSizeTruncated) {
        throw new TooLargeFile();
      }

      @SuppressWarnings("null")
      final @NonNull ByteBuffer fileContent = ByteBuffer.allocate(fileSizeTruncated);
      channel.read(fileContent);
      return fileContent;
    } catch (IOException e) {
      throw new StorageInaccessibleError();
    }

  }

  private SeekableByteChannel getFileChannel() throws StorageInaccessibleError {
    // verify that file exists
    if (!Files.exists(path)) {
      // create all missing parent directories
      // file itself will be created by open options
      // if could not create parent dirs, report what when wrong (perm)
      try {
        PathHelpers.createParentDirectories(path);
      } catch (FileSystemException e) {
        // TODO: pass error to clarify what went wrong
        throw new StorageInaccessibleError();
      }
    }

    try {
      @SuppressWarnings("null")
      final @NonNull SeekableByteChannel channel = Files.newByteChannel(path, FileStorage.DEFAULT_FILE_OPEN_OPTIONS);
      return channel;
    } catch (IOException e) {
      throw new StorageInaccessibleError();
    }

  }

  @Override
  public Observable<C> load(BaseId key) {
    return findCollectionById(key, this.load());
  }

  @Override
  public Observable<C> loadAll() {
    return this.load();
  }

  @Override
  public Observable<CollectionMetadata> loadMetadata() {
    return this.load().map(collection -> collection.getPersistentMetadata());
  }

  @Override
  public Observable<CollectionMetadata> loadMetadata(BaseId key) {
    return findCollectionById(key, this.load()).map(collection -> collection.getPersistentMetadata());
  }

  private Observable<C> findCollectionById(BaseId key, Observable<C> collections) {
    return collections.filter((collection) -> collection.getPersistentMetadata().getId().equals(key)).singleOrError()
        .onErrorResumeNext(error -> {
          if (error instanceof NoSuchElementException) {
            return Single.error(new CollectionNotFound());
          }

          return Single.error(error);
        }).toObservable();
  }

  @Override
  public Observable<C> loadSafe(CollectionMetadata metadata) {
    return this.load(metadata.getId()).onErrorResumeNext(error -> {
      if (error instanceof CollectionNotFound) {
        return Observable.just(adapter.deserializeSafe(metadata));
      }

      return Observable.error(error);
    });
  }

  // private void initEmptyCollection

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
