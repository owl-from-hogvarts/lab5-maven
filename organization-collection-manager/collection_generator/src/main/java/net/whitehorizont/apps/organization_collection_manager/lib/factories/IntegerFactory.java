package net.whitehorizont.apps.organization_collection_manager.lib.factories;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import net.whitehorizont.apps.organization_collection_manager.lib.IDisplayable;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;

@NonNullByDefault
public class IntegerFactory<Num extends Number> implements IFromStringBuilder<Num> {

  private final Class<Num> numberFactory;
  private final NumberExample numberExample;

  private static final NumberExample DEFAULT_EXAMPLE = new NumberExample("number", null);
  private static final Map<Class<? extends Number>, NumberExample> EXAMPLES_MAP = new HashMap<>();

  static {
    EXAMPLES_MAP.put(Integer.class, new NumberExample("integer", "Expected something like 123"));
    EXAMPLES_MAP.put(Long.class, new NumberExample("long", "Expected something like 123"));
    EXAMPLES_MAP.put(Float.class, new NumberExample("float", "Expected something like 123.456"));
    EXAMPLES_MAP.put(Double.class, new NumberExample("double", "Expected something like 123.456"));

  }

  private static class NumberExample implements IDisplayable {
    @Nullable private final String example;
    private final String name;

    private NumberExample(String name, @Nullable String example) {
      this.name = name;
      this.example = example;
    }

    @Override
    public String getDisplayedName() {
      return name;
    }
  }

  public IntegerFactory(Class<Num> numberFactory) {
    this.numberFactory = numberFactory;

    this.numberExample = EXAMPLES_MAP.getOrDefault(numberFactory, DEFAULT_EXAMPLE);
  }

  @Override
  public Num buildFromString(String string) throws ValidationError {
    try {
      try {
        return (Num) numberFactory.getMethod("valueOf", String.class).invoke(numberFactory, string);
      } catch (InvocationTargetException ITE) {
        // https://stackoverflow.com/a/6020758/13167052
        final var e = (NumberFormatException) ITE.getCause();
        throw new ValidationError(
            "String \"" + string + "\" is not an " + this.numberExample.name + " number.\n"
                + (numberExample.example != null ? numberExample.example + "\n" : "")
                + "Number should be within range: " + numberFactory.getField("MIN_VALUE").get(numberFactory).toString()
                + " <= number <= "
                + numberFactory.getField("MAX_VALUE").get(numberFactory).toString());
      }
    } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException _ignore) {
      // похуй
      assert false;
      throw new RuntimeException();
    }
  }

}
