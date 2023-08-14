package net.whitehorizont.apps.collection_manager.core.commands;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadata;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition.CollectionMetadataComputed;
import net.whitehorizont.apps.collection_manager.core.collection.errors.NoSuchElement;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.collection_manager.core.collection.keys.ElementKey;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public interface ICollectionCommandReceiver<E extends ICollectionElement<E>> extends ICollection<E> {

  void replace(ElementKey key, E element) throws ValidationError, NoSuchElement;

  E delete(ElementKey key) throws NoSuchElement;

  Observable<E> getEvery$();

  Observable<Entry<ElementKey, E>> getEveryWithKey$();

  Observable<List<E>> getAll$();

  CollectionMetadata getPersistentMetadata();

  CollectionMetadataComputed getMetadataTree();

  void clear();

  ElementKey getElementKeyFromString(String keyString) throws ValidationError;

  String getCollectionType();

  void insert(E element) throws Exception;

  void insert(ElementKey key, E element) throws Exception;

}