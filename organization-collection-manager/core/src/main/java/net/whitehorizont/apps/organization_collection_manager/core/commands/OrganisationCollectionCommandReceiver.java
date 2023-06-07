package net.whitehorizont.apps.organization_collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.IElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.NoSuchElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationType;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.organization_collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Specialized collection receiver for Organisation type of collection
 */
@NonNullByDefault
public class OrganisationCollectionCommandReceiver extends CollectionCommandReceiver<OrganisationElementPrototype, OrganisationElement> {

  public OrganisationCollectionCommandReceiver(ICollection<OrganisationElementPrototype, OrganisationElement> collection) {
    super(collection);
  }

  public Observable<Void> replaceById(BaseId id, IPrototypeCallback<OrganisationElementPrototype> callback) {
    final var entries = this.collection.getEveryWithKey$()
    .filter(keyElement -> keyElement.getValue().getID().getValue().equals(id));
    final var amount = entries.count().blockingGet();
    if (amount < 1) {
      return Observable.error(new NoSuchElement(id));
    }

    return entries.flatMap(keyElement -> {
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
    return this.collection.getEvery$().filter(element -> element.getType().getValue().equals(type)).count();
  }

  public void removeById(UUID_ElementId id) {
    this.collection.getEveryWithKey$()
      .filter(keyElement -> keyElement
                              .getValue()
                              .getID()
                              .getValue()
                              .equals(id))
      .map(keyElement -> keyElement.getKey())
      .blockingSubscribe(key -> this.collection.delete(key));
  }

  public static interface IPrototypeCallback<P extends IElementPrototype<?>> {
    P apply(P prototype) throws ValidationError;
  }

  public void removeByRevenue(RemovalCriteria removalCriteria, double targetValue) {
    this.getEveryWithKey$().filter(keyElement -> {
      final @NonNull var currentAnnualTurnover = keyElement.getValue().getAnnualTurnover().getValue();
      return switch (removalCriteria) {
        case ABOVE -> currentAnnualTurnover > targetValue;
        case BELOW -> currentAnnualTurnover < targetValue;
        default -> {throw new RuntimeException();}
      };
    })
    .map(keyElement -> keyElement.getKey())
    .subscribe(key -> collection.delete(key));
  }

  public enum RemovalCriteria {
    BELOW,
    ABOVE
  }
}
