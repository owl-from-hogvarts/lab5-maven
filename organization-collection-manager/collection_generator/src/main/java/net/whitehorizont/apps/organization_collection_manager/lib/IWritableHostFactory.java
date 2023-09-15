package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IWritableHostFactory<WritableHost> {
  /**
   * Creates and returns new instance of {@code WritableHost}
   * 
   * @return
   */
  WritableHost createWritable();
}
