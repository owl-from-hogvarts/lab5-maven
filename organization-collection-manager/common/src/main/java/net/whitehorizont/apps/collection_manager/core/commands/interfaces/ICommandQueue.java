package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public interface ICommandQueue<DependencyManager> {

  <@NonNull T> Observable<T> push(ICommand<T, ? super DependencyManager> command);

}