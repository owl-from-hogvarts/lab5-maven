package net.whitehorizont.libs.result;

import java.io.Serializable;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class Result<T> implements Serializable, Cloneable {
  private final @Nullable T result;
  private final @Nullable Throwable error;
  
  public Result(Throwable error) {
    this.result = null;
    this.error = error;
  }

  public Result(T result) {
    this.error = null;
    this.result = result;
  }

  public boolean isError() {
    return error != null;
  }

  public boolean isSuccess() {
    return !isError();
  }

  /**
   * Check before call
   */
  public Throwable getError() {
    return error;
  }

  /**
   * Check before calls
   */
  public T getResult() {
    return result;
  }


}
