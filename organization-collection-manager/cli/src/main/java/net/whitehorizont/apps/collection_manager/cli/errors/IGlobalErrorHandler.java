package net.whitehorizont.apps.collection_manager.cli.errors;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;

@NonNullByDefault
@FunctionalInterface
public interface IGlobalErrorHandler {
  /**
   * 
   * @param e
   * @param dependencyManager
   * @return {@code true} stop interrupts execution of current CLI instance
   */
  boolean handle(Throwable e, CliDependencyManager<?> dependencyManager);
}
