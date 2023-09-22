package net.whitehorizont.apps.collection_manager.core.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.javatuples.Pair;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ISerializableKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.KeyGenerationError;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_CollectionId;
import net.whitehorizont.apps.collection_manager.core.collection.middleware.CollectionMiddleware;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.organization_collection_manager.lib.IElementInfoProvider;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Holds collection in memory, without auto saving to disk
 */
@NonNullByDefault
public class RamCollection<E extends ICollectionElement<E>>
    implements ICollection<E> {

  private static final int MAX_BRUT_FORCE_RETRIES = 10;


  private final Map<ElementKey, E> elements = new LinkedHashMap<>();
  private final CollectionMetadata metadata;
  private final IElementInfoProvider<E> elementMetadata;
  private final List<CollectionMiddleware<E>> insertMiddleware;
  private final List<CollectionMiddleware<E>> deleteMiddleware;
  
  private RamCollection(Configuration<E, ?> configuration, Optional<CollectionMetadata> metadata) {
    this.metadata = metadata.isEmpty() ? new CollectionMetadata(new UUID_CollectionId()) : metadata.get();
    this.elementMetadata = configuration.elementMetadata;
    this.insertMiddleware = configuration.insertMiddleware;
    this.deleteMiddleware = configuration.deleteMiddleware;
  }

  /**
   * 
   * @throws ValidationError
   * @throws KeyGenerationError
   * @throws DuplicateElements
   * @throws StorageInaccessibleError
   * @throws NoSuchElement
   * @throws DuplicateElementsError
   */
  @Override
  public void insert(E element) throws ValidationError, DuplicateElements, KeyGenerationError, StorageInaccessibleError {
    insert(generateElementKey(), element);
  }


  @Override
  public void insert(ElementKey key, E element) throws ValidationError, DuplicateElements, StorageInaccessibleError {
    if (containsKey(key)) {
      throw new DuplicateElements(key);
    }

    for (final var singleMiddleware : insertMiddleware) {
      singleMiddleware.accept(this, element);
    }
    // replace with middleware
    this.elements.put(key, element);
  }

  @Override
  public Observable<E> getEvery$() {
    return Observable.fromIterable(elements.values());
  }

  @Override
  public Observable<Pair<ElementKey, E>> getEveryWithKey$() {
    return Observable.just(elements).flatMap((elements) -> {
      @SuppressWarnings("null")
      final @NonNull Set<Entry<ElementKey, E>> keyValuePairs = elements.entrySet();

      return Observable.fromIterable(keyValuePairs).map(keyValue -> new Pair<>(keyValue.getKey(), keyValue.getValue()));
    });
  }

  @Override
  public Observable<List<E>> getAll$() {
    return Observable.just(new ArrayList<>(elements.values()));
  }

  @Override
  public CollectionMetadata getPersistentMetadata() {
    return this.metadata;
  }

  @Override
  public void clear() {
    this.elements.clear();
  }

  ElementKey generateElementKey() throws KeyGenerationError {
    ElementKey key;
    int tried = 0;
    do {
      tried += 1;
      key = ElementKey.next();
    } while (containsKey(key) && tried <= MAX_BRUT_FORCE_RETRIES);

    if (containsKey(key)) {
      key = ElementKey.next(this.elements.keySet().stream());
    }

    return key;
  }

  private boolean containsKey(ElementKey key) {
    return this.elements.containsKey(key);
  }

  @Override
  public void replace(ElementKey key, E element) throws ValidationError, NoSuchElement, StorageInaccessibleError {
    checkIfExists(key);
    final var removed = this.delete(key);
    try {
      try {
        this.insert(key, element);
      } catch (ValidationError e) {
        // add back in case of failure
        this.insert(key, removed);
        throw e;
      }
    } catch (DuplicateElements e) {
      // replace is not atomic. actually may happen
      // either lock or implement compare and swap
      assert false;
      throw new RuntimeException(e);
    }
  }

  private void checkIfExists(ISerializableKey key) throws NoSuchElement {
    if (!this.elements.containsKey(key)) {
      throw new NoSuchElement(key);
    }
  }

  @Override
  public E delete(ElementKey key) throws NoSuchElement, ValidationError, StorageInaccessibleError {
    checkIfExists(key);

    final var element = this.elements.get(key);

    for (final var singleMiddleware : deleteMiddleware) {
      singleMiddleware.accept(this, element);
    }

    return this.elements.remove(key);
  }

  @Override
  public ElementKey getElementKeyFromString(String keyString) throws ValidationError {
    return ElementKey.buildFromString(keyString);
  }

  @Override
  public String getCollectionType() {
    return this.elementMetadata.getDisplayedName();
  }

  public void registerInsert(CollectionMiddleware<E> middleware) {
    this.insertMiddleware.add(middleware);
  }

  public void registerDelete(CollectionMiddleware<E> middleware) {
    this.deleteMiddleware.add(middleware);
  }

  public static class Configuration<E extends ICollectionElement<E>, This extends Configuration<E, This>> implements Cloneable {
    private Optional<CollectionMetadata> metadata = Optional.empty();
    private @Nullable IElementInfoProvider<E> elementMetadata;
    private @Nullable List<CollectionMiddleware<E>> insertMiddleware;
    private @Nullable List<CollectionMiddleware<E>> deleteMiddleware;
    
    private This self() {
      return (This) this;
    }

    public This metadata(CollectionMetadata metadata) {
      this.metadata = Optional.of(metadata);
      return self();
    }

    public This elementMetadata(IElementInfoProvider<E> elementMetadata) {
      this.elementMetadata = elementMetadata;
      return self();
    }

    public This insertMiddleware(List<CollectionMiddleware<E>> middleware) {
      this.insertMiddleware = middleware;
      return self();
    }

    public This deleteMiddleware(List<CollectionMiddleware<E>> middleware) {
      this.deleteMiddleware = middleware;
      return self();
    }

    public RamCollection<E> build() {
      return build(this.metadata);
    }

    public RamCollection<E> build(Optional<CollectionMetadata> collectionMetadata) {
      checkNull("elementMetadata", elementMetadata);
      checkNull("insertMiddleware", insertMiddleware);
      checkNull("deleteMiddleware", deleteMiddleware);
      final var collection = new RamCollection<>(this, collectionMetadata);

      return collection;
    }

    private void checkNull(String fieldName, @Nullable Object any) {
      if (any == null) {
        throw new InvalidBuilderConfiguration(fieldName);
      }
    }

    @Override
    public This clone() {
      try {
        return (This) super.clone();
      } catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
