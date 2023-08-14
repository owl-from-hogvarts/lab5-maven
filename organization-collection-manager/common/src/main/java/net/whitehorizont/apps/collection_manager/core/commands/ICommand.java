package net.whitehorizont.apps.collection_manager.core.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public interface ICommand<Return, Receiver> {
  /** Always ensure that observable completes */
  Observable<Return> execute(Receiver receiver);
}
