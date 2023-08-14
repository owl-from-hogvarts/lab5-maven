package net.whitehorizont.apps.collection_manager.cli.errors;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
@FunctionalInterface
public interface IInterruptHandler {
  Observable<Void> handle() throws Exception;
}
