package net.whitehorizont.apps.organization_collection_manager.core.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.NoSuchElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementDefinition;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationType;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Specialized collection receiver for Organisation type of collection
 */
@NonNullByDefault
public class OrganisationCollectionCommandReceiver extends CollectionCommandReceiver<OrganisationElementDefinition> {

  public OrganisationCollectionCommandReceiver(ICollection<OrganisationElementDefinition> collection) {
    super(collection);
  }

  public Observable<Void> replaceById(BaseId id, IPrototypeCallback<OrganisationElementPrototype> callback) {
    final var entries = this.collection.getEveryWithKey$()
    .filter(keyElement -> keyElement.getValue().getID().getValue().equals(id));
    final var amount = entries.count().blockingGet();
    if (amount < 1) {
      return Observable.error(new NoSuchElement(id));
    }

    // iteration would happen over freshly constructed list so 
    // no concurrent modification exception should happen
    return entries.toList().flatMapObservable(list -> Observable.fromIterable(list)).flatMap(keyElement -> {
      final var prototype = keyElement.getValue().getPrototype();
      try {
        final var updatedPrototype = callback.apply(prototype);
        final var key = keyElement.getKey();
        this.collection.replace(key, updatedPrototype);
        return Observable.empty();
      } catch (ValidationError | NoSuchElement e) {
        return Observable.error(e);
      }
    });
  }

  public Single<Long> countByType(OrganisationType type) {
    return this.collection.getEvery$().filter(element -> OrganisationElementDefinition.TYPE_METADATA.getValueGetter().apply(element) == type).count();
  }

  public void removeById(UUID_ElementId id) {
    this.collection.getEveryWithKey$()
      .filter(keyElement -> keyElement
                              .getValue()
                              .getID()
                              .getValue()
                              .equals(id))
      .map(keyElement -> keyElement.getKey())
      // blocking prevents concurrent modification
      .blockingSubscribe(key -> this.collection.delete(key));
  }

  public static interface IPrototypeCallback<P extends IElementPrototype<?>> {
    P apply(P prototype) throws ValidationError;
  }

  public void removeByRevenue(RemovalCriteria removalCriteria, double targetValue) {
    final var keysToDelete = this.getEveryWithKey$().filter(keyElement -> {
      final @NonNull var currentAnnualTurnover = keyElement.getValue().getAnnualTurnover().getValue();
      return switch (removalCriteria) {
        case ABOVE -> currentAnnualTurnover > targetValue;
        case BELOW -> currentAnnualTurnover < targetValue;
        default -> {throw new RuntimeException();}
      };
    })
    .map(keyElement -> keyElement.getKey()).toList().blockingGet();

    for (final var key : keysToDelete) {
      try {
        this.collection.delete(key);
      } catch (Exception _ignore) {
        // should never happen
        assert false;
        throw new RuntimeException();
      }
    }
  }

  public Observable<Entry<ElementKey, OrganisationElementDefinition>> getStartsWith$(String startOfFullName) {
    return collection.getEveryWithKey$().filter(keyElement -> keyElement.getValue().getName().getValue().startsWith(startOfFullName));
  }

  public enum RemovalCriteria {
    BELOW,
    ABOVE
  }

  public Observable<Entry<ElementKey, OrganisationElementDefinition>> getDescending$() {
    return this.collection.getEveryWithKey$().sorted((a, b)-> {
      final var aElement = a.getValue();
      final var bElement = b.getValue();
      // b to a to reverse order
      return bElement.compareTo(aElement);
    });
  }
}
