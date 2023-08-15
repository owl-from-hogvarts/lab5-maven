package net.whitehorizont.apps.collection_manager.organisation.commands;

import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.whitehorizont.apps.collection_manager.core.collection.keys.BaseId;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.collection_manager.core.collection.keys.UUID_ElementId;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationType;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElement;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementWritable;

@NonNullByDefault
public interface IOrganisationCollectionCommandReceiver extends ICollectionCommandReceiver<OrganisationElement> {

  Observable<Void> replaceById(BaseId id, OrganisationElementWritable prototype);

  Single<Long> countByType(OrganisationType type);

  void removeById(UUID_ElementId id);

  void removeByRevenue(RemovalCriteria removalCriteria, double targetValue);

  Observable<Entry<ElementKey, OrganisationElement>> getStartsWith$(String startOfFullName);

  Observable<Entry<ElementKey, OrganisationElement>> getDescending$();

  public static enum RemovalCriteria {
    BELOW,
    ABOVE
  }
}