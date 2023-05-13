package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class CommandArgumentDescriptor {
  private final boolean isNullable;

  public CommandArgumentDescriptor(boolean isNullable) {
    this.isNullable = isNullable;
  }

  public boolean isNullable() {
    return isNullable;
  }
}
