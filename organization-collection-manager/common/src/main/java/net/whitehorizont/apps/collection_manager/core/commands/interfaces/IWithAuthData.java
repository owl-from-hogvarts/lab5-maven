package net.whitehorizont.apps.collection_manager.core.commands.interfaces;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

@NonNullByDefault
public interface IWithAuthData<Return, DP> extends ICommand<Return,  DP> {
  Observable<@NonNull Return> executeWithAuthData(DP dependencyProvider, String login);
}
