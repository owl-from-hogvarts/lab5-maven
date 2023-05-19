package net.whitehorizont.apps.organization_collection_manager.lib;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class StringFactory implements IFromStringBuilder<String> {

  @Override
  public String buildFromString(String string) {
    return string;
  }
  
}
