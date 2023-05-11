package net.whitehorizont.apps.organization_collection_manager.cli.commands;

public class CommandArgumentDescriptor {
  private final String name;
  private final boolean isNullable;

  public CommandArgumentDescriptor(String name, boolean isNullable) {
    this.name = name;
    this.isNullable = isNullable;
  }

  public String getName() {
    return name;
  }

  public boolean isNullable() {
    return isNullable;
  }
}
