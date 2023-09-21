package net.whitehorizont.apps.collection_manager.core.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import net.whitehorizont.apps.organization_collection_manager.lib.ICanRichValidate;
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
  private final List<ICanRichValidate<E, ? super RamCollection<E>>> validators = new ArrayList<>();

  public RamCollection(IElementInfoProvider<E> elementMetadata, @Nullable CollectionMetadata metadata) {
    this.metadata = metadata == null ? new CollectionMetadata(new UUID_CollectionId()) : metadata;
    this.elementMetadata = elementMetadata;
  }

  public RamCollection(IElementInfoProvider<E> elementMetadata) {
    this(elementMetadata, null);
  }

  /**
   * 
   * @throws ValidationError
   * @throws KeyGenerationError
   * @throws DuplicateElements
   * @throws NoSuchElement
   * @throws DuplicateElementsError
   */
  @Override
  public void insert(E element) throws ValidationError, DuplicateElements, KeyGenerationError {
    insert(generateElementKey(), element);
  }


  @Override
  public void insert(ElementKey key, E element) throws ValidationError, DuplicateElements {
    if (containsKey(key)) {
      throw new DuplicateElements(key);
    }

    this.validateElement(element);
    // replace with middleware
    this.elements.put(key, element);
  }

  /**
   * Registers validator for this collection
   * 
   * @param validator will be executed on each insert
   */
  public void addValidator(ICanRichValidate<E, ? super RamCollection<E>> validator) {
    this.validators.add(validator);
  }

  private void validateElement(E element) throws ValidationError {
    // execute simple validators attached via metadata
    this.elementMetadata.validate(element);
    
    // validate with rich validators
    for (final var validator : this.validators) {
      validator.validate(element, this);
    }
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
  public void replace(ElementKey key, E element) throws ValidationError, NoSuchElement {
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
  public E delete(ElementKey key) throws NoSuchElement {
    checkIfExists(key);
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

  public static class Configuration<E extends ICollectionElement<E>, This extends Configuration<E, This>> {
    private CollectionMetadata metadata;
    private IElementInfoProvider<E> elementMetadata;
    private List<ICanRichValidate<E, ? super RamCollection<E>>> validators = new ArrayList<>();
    
    public Configuration() {
    }

    private This self() {
      return (This) this;
    }

    public This metadata(CollectionMetadata metadata) {
      this.metadata = metadata;
      return self();
    }

    public This elementMetadata(IElementInfoProvider<E> elementMetadata) {
      this.elementMetadata = elementMetadata;
      return self();
    }

    public This validators(List<ICanRichValidate<E, ? super RamCollection<E>>> validators) {
      this.validators = validators;
      return self();
    }

    public RamCollection<E> build() {
      checkNull("elementMetadata", elementMetadata);
      checkNull("validators", validators);
      final var collection = new RamCollection<>(elementMetadata, metadata);
      for (final var validator : validators) {
        collection.addValidator(validator);
      }

      return collection;
    }

    private void checkNull(String fieldName, @Nullable Object any) {
      if (any == null) {
        throw new InvalidBuilderConfiguration(fieldName);
      }
    }
  }
}
