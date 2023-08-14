package net.whitehorizont.apps.collection_manager.core.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.core.commands.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationType;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElement;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;
import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

/**
 * Specialized collection receiver for Organisation type of collection
 */
@NonNullByDefault
public class OrganisationCollectionCommandReceiver implements ICollectionCommandReceiver<OrganisationElement>, IOrganisationCollectionCommandReceiver {
  final ICollection<OrganisationElement> collection;

  public OrganisationCollectionCommandReceiver(ICollection<OrganisationElement> collection) {
    this.collection = collection;
  }

  @Override
  public Observable<Void> replaceById(BaseId id, OrganisationElementWritable prototype) {
    final var entries = this.collection.getEveryWithKey$()
        .filter(keyElement -> OrganisationElementDefinition.ID_METADATA.getValueGetter().apply(keyElement.getValue())
            .equals(id));
    final var amount = entries.count().blockingGet();
    if (amount < 1) {
      return Observable.error(new NoSuchElement(id));
    }

    // iteration would happen over freshly constructed list so
    // no concurrent modification exception should happen
    return entries.toList().flatMapObservable(list -> Observable.fromIterable(list)).flatMap(keyElement -> {
      final var base = keyElement.getValue();
      try {
        final var updatedPrototype = new OrganisationElementWritable();
        // fill updatedPrototype with values from base
        OrganisationElementDefinition.getMetadata().fill(updatedPrototype, base);
        // update all fields except with PRESERVE tag
        OrganisationElementDefinition.getMetadata().fill(updatedPrototype, prototype, Tag.PRESERVE);
        // do replace
        final var key = keyElement.getKey();
        this.collection.replace(key, updatedPrototype);
        return Observable.empty();
      } catch (ValidationError | NoSuchElement e) {
        return Observable.error(e);
      }
    });
  }

  @Override
  public Single<Long> countByType(OrganisationType type) {
    return this.collection.getEvery$()
        .filter(element -> OrganisationElementDefinition.TYPE_METADATA.getValueGetter().apply(element) == type).count();
  }

  @Override
  public void removeById(UUID_ElementId id) {
    this.collection.getEveryWithKey$()
        .filter(keyElement -> OrganisationElementDefinition.ID_METADATA.getValueGetter()
            .apply(keyElement.getValue())
            .equals(id))
        .map(keyElement -> keyElement.getKey())
        // blocking prevents concurrent modification
        .blockingSubscribe(key -> this.collection.delete(key));
  }

  @Override
  public void removeByRevenue(RemovalCriteria removalCriteria, double targetValue) {
    final var keysToDelete = this.getEveryWithKey$().filter(keyElement -> {
      final @NonNull var currentAnnualTurnover = OrganisationElementDefinition.ANNUAL_TURNOVER_METADATA.getValueGetter()
          .apply(keyElement.getValue());
      return switch (removalCriteria) {
        case ABOVE -> currentAnnualTurnover > targetValue;
        case BELOW -> currentAnnualTurnover < targetValue;
        default -> {
          throw new RuntimeException();
        }
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

  @Override
  public Observable<Entry<ElementKey, OrganisationElement>> getStartsWith$(String startOfFullName) {
    return collection.getEveryWithKey$().filter(keyElement -> OrganisationElementDefinition.NAME_METADATA
        .getValueGetter().apply(keyElement.getValue()).startsWith(startOfFullName));
  }

  @Override
  public Observable<Entry<ElementKey, OrganisationElement>> getDescending$() {
    return this.collection.getEveryWithKey$().sorted((a, b) -> {
      final var aElement = a.getValue();
      final var bElement = b.getValue();
      // b to a to reverse order
      return bElement.compareTo(aElement);
    });
  }
}
