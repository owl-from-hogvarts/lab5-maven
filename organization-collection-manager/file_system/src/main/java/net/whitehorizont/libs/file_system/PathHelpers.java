package net.whitehorizont.libs.file_system;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

import net.whitehorizont.libs.file_system.errors.PathSegmentError;

public final class PathHelpers {
  static public Path preparePath(Path path) {
    return path.toAbsolutePath().normalize();
  }

  static public Path createParentDirectories(Path pathToFile) throws FileSystemException {
    final Path pathToFinalDirectory = getParentSafe(pathToFile);

    Path accessiblePath = pathToFinalDirectory.getRoot();
    Path resolved = accessiblePath;
    try {
      for (Path segment : pathToFinalDirectory) {
        resolved = accessiblePath.resolve(segment);

        // ! all errors should be reported for resolved path
        if (!Files.isDirectory(resolved)) {
          throw new NotDirectoryException(resolved.toString());
        }

        if (!Files.exists(resolved)) {
          try {
            Files.createDirectory(resolved);
          } catch (FileSystemException e) {
            throw e;
          } catch (IOException e) {
            assert false : AssertHelpers.getAssertMessageFor("PathHelpers", "createParentDirectories");
            // just ignore it
            // lets pray that IOException will never be thrown
          }
        }

        accessiblePath = resolved;
      }
    } catch (FileSystemException e) {
      throw new PathSegmentError(pathToFinalDirectory, resolved, e);
    }

    return pathToFile;

  }

  static public Path getParentSafe(Path path) {
    if (path.getParent() == null) {
      return path;
    }

    return path.getParent();
  }

  public static Path getSubpathSafe(Path path, int start, int end) {
    if (start > end) {
      final int tmp = end;
      end = start;
      start = tmp;
    }

    if (path.getNameCount() == 0) {
      return path;
    }

    return path.subpath(start, end);
  }
}
