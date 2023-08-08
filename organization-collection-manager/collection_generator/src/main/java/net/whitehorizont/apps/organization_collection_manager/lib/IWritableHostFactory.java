package net.whitehorizont.apps.organization_collection_manager.lib;

public interface IWritableHostFactory<WritableHost> {
  /**
   * Creates and returns new instance of {@code WritableHost}
   * 
   * @return
   */
  WritableHost createWritable();
}
