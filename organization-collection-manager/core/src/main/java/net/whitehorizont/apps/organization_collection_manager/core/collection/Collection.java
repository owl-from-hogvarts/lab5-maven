package net.whitehorizont.apps.organization_collection_manager.core.collection;

public class Collection<E> {

  /**
   * Collection listens on returned sink to receive new elements
   */
  public void getDataSink() {
  }

  // stores creation time
  public void getMetadataSnapshot() {}

  public void clear() {}

  // when new elements arrives from data sink
  // check it's integrity
  // report if it is modified
  // if duplicate elements are encountered, warn a user
  // ask if he would like to remove or store duplicates
  // if user prefers to store duplicates, change the seed of element which arrived later
  // if user agrees to proceed, update id and add element to collection
}
