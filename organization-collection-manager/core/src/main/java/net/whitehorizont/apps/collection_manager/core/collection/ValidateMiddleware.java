package net.whitehorizont.apps.collection_manager.core.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollectionElement;
import net.whitehorizont.apps.organization_collection_manager.lib.ICanRichValidate;
import net.whitehorizont.apps.organization_collection_manager.lib.IElementInfoProvider;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class ValidateMiddleware<E extends ICollectionElement<E>> implements CollectionMiddleware<E> {

  private final List<ICanRichValidate<E, ICollection<E>>> richValidators = new ArrayList<>();
  private final IElementInfoProvider<E> elementValidators;

  public ValidateMiddleware(IElementInfoProvider<E> elementValidators) {
    this.elementValidators = elementValidators;
  }

  public void addRichValidator(ICanRichValidate<E, ICollection<E>> validator) {
    this.richValidators.add(validator);
  }

  @Override
  public void accept(ICollection<E> collection, E element) throws ValidationError {
    this.elementValidators.validate(element);

    for (final var validator : richValidators) {
      validator.validate(element, collection);
    }
  }
  
}
