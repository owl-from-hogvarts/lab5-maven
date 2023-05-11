package net.whitehorizont.apps.organization_collection_manager.lib;

public class NumberFactory implements IFromStringBuilder<Integer> {

  @Override
  public Integer buildFromString(String string) {
    return Integer.valueOf(string);
  }
  
}
