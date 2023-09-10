package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.PermissionError;

@NonNullByDefault
public interface ICommandQueue<DependencyManager> {

  <@NonNull T> Observable<T> push(ICommand<T, ? super DependencyManager> command) throws PermissionError;

}