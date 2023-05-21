package net.whitehorizont.apps.organization_collection_manager.lib;

public class EnumFactory<E extends Enum<E>> implements IFromStringBuilder<E> {

  private final Class<E> enumClass;

  public EnumFactory(Class<E> e) {
    enumClass = e;
  }

  @Override
  public E buildFromString(String string) throws ValidationError {
    return Enum.valueOf(enumClass, string);
  }
  
}


enum Test {

}

class A {
  {
  }
}