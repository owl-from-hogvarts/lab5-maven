package net.whitehorizont.libs.file_system.errors;

import java.nio.file.FileSystemException;
import java.nio.file.Path;

import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.libs.file_system.AssertHelpers;

public class PathSegmentError extends FileSystemException {
  private static final String DEFAULT_ERROR_REASON = "Unknown Error";


  private final Path fullPath;
  private final Path failedSubpath;

  /** Use when sure that error belongs to `path` */
  public PathSegmentError(Path path) {
    this(path, null);
  }

  public PathSegmentError(Path path, @Nullable FileSystemException cause) {
    this(path, path, cause);
  }

  static private String getErrorReasonSafe(@Nullable FileSystemException cause) {
    return (cause != null ? cause.getReason() : DEFAULT_ERROR_REASON);
  }

  public PathSegmentError(Path fullPath, Path failedSubpath, @Nullable FileSystemException cause) {
    super(fullPath.toString(), failedSubpath.toString(), getErrorReasonSafe(cause));
    assert !fullPath.startsWith(failedSubpath)
        : AssertHelpers.getAssertMessageFor("PathSegmentError.java", "constructor");
    this.fullPath = fullPath;
    this.failedSubpath = failedSubpath;

    initCause(cause);
  }

  public Path getFailedSubpath() {
    return failedSubpath;
  }

  public Path getFullPath() {
    return fullPath;
  }
}
