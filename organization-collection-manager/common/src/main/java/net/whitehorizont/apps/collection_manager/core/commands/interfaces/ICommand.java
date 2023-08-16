package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

/**
 * Command receivers and other important data can be obtained by 
 * accessing dependency provider,
 * passed to {@code execute()} method
 * 
 * @param <Return> observable of the type
 * @param <DependencyProvider> provide an interface, which is implemented by dependency manager
 * 
 */
@NonNullByDefault
public interface ICommand<Return, DependencyProvider> {
  /** Always ensure that observable completes */
  Observable<@NonNull Return> execute(DependencyProvider dependencyProvider);
  default boolean isServerOnly() {
    return false;
  }
}
