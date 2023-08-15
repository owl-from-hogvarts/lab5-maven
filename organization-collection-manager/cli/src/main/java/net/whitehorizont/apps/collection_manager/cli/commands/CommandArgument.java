package net.whitehorizont.apps.collection_manager.cli.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class CommandArgument {
  private final String value;

  public String getValue() {
    return value;
  }

  public CommandArgument(String value) {
    this.value = value;
  }
  
}
