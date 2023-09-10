package net.whitehorizont.apps.collection_manager.cli.errors;

public class IncorrectNumberOfArguments extends Exception {
  public IncorrectNumberOfArguments(String command, int expected, int actual) {
    super(buildErrorMessage(command, expected, actual));
  }

  private static String buildErrorMessage(String command, int expected, int actual) {
    return "Incorrect number of arguments supplied for command \"" + command + "\". Expected " + expected + ". Got "
        + actual;
  }

}
