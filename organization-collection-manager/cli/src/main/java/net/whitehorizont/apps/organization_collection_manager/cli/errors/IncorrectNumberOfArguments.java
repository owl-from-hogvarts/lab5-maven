package net.whitehorizont.apps.organization_collection_manager.cli.errors;

public class IncorrectNumberOfArguments extends Exception {
  private final int expected;
  private final int actual;
  private final int max;
  private final String command;

  public IncorrectNumberOfArguments(String command, int expected, int actual, int max) {
    this.command = command;

    assert expected <= max;
    this.expected = expected;
    this.actual = actual;
    this.max = max;
  }

  @Override
  public String getMessage() {
    return "Incorrect number of arguments for command \"" + command + "\" supplied. Expected at least" + expected + ". Got "
        + actual + ".\nMaximum allowed arguments " + max;
  }

}
