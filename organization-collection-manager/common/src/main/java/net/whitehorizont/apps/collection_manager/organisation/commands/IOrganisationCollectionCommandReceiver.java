package net.whitehorizont.apps.collection_manager.organisation.commands;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.collection_manager.core.collection.errors.DuplicateElements;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationType;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface IOrganisationCollectionCommandReceiver extends ICollectionCommandReceiver<OrganisationElementFull> {

  Observable<Void> replaceById(BaseId id, OrganisationElementWritable prototype);

  Single<Long> countByType(OrganisationType type);

  void removeById(UUID_ElementId id);

  void removeByRevenue(RemovalCriteria removalCriteria, double targetValue) throws ValidationError, StorageInaccessibleError;

  Observable<Pair<ElementKey, OrganisationElementFull>> getStartsWith$(String startOfFullName);

  Observable<Pair<ElementKey, OrganisationElementFull>> getDescending$();

  public static enum RemovalCriteria {
    BELOW,
    ABOVE
  }

  void insert(String key, OrganisationElementWritable element) throws ValidationError, DuplicateElements, StorageInaccessibleError;
}