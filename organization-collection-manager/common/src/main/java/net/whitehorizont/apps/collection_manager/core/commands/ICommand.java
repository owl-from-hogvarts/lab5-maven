package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

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
  Observable<Return> execute(DependencyProvider dependencyProvider);
}
