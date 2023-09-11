package net.whitehorizont.libs.network.serialize;

public class UnparsableResponse extends Exception {
  private static final String MESSAGE = "Could not parse response!";
  
  public UnparsableResponse(Throwable cause) {
    super(MESSAGE, cause);
  }
}
