package net.whitehorizont.apps.organization_collection_manager.core.storage;

// provides adapter with file content
// adapters operate on collections
public class FileStorage {
  public FileStorage(String path) {
    // create file object
    // verify that file exists
    // if file does not exist, create it
      // create all missing parent directories
      // if could not create parent dirs, report what when wrong (perm)
    // may be just use iterators
    // 
    // if file exists
      // create observable
      // apply delayWhen(ready)
      // emit two observables and complete
      // one observable represents warnings
      // the other one represents collections
      // verify integrity
      // check if hash sum stored in file match the computed one
    // error if data was modified (integrity check failed) by external factors
    // report that resource is available
  }

  public void save() {
    // receive collection to store
    // compute hash sum for collection
    // prepare hash sum for storing
    // serialize collection
    // check if file is still available
    // if file is not available, do the exact same thing as in constructor
    // if everything ok, check if enough space is available for writing
    // write down serialized data
  }

  public void load() {
    // receive collection id
    // if no collection id specified, consider default or all collections
    // storage = new FileStorage() // internally delayWhen 

    
  }
}
