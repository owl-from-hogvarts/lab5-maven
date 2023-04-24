package net.whitehorizont.apps.organization_collection_manager.core.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PathHelpers {
  static public Path preparePath(Path path) {
    return path.toAbsolutePath().normalize();
  }
  
  static public Path createParentDirectories(Path pathToFile) {
    final Path pathToFinalDirectory = getParentSafe(pathToFile);

    Path accessiblePath = pathToFinalDirectory.getRoot(); 
    for (Path segment : pathToFinalDirectory) {
        Path resolved = accessiblePath.resolve(segment);
        if (!Files.exists(resolved)) {
          try {
            Files.createDirectory(resolved);
            accessiblePath = resolved;
          } catch (IOException e) {
            // TODO: handle execptions
          }
          
        }
        
      }
    
    return pathToFile;

  }

  static public Path getParentSafe(Path path) {
    if (path.getParent() == null) {
      return path;
    }

    return path.getParent();
  }
}
