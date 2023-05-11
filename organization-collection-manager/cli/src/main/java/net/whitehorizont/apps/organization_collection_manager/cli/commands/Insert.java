package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;

@NonNullByDefault
public class Insert<P> implements ICliCommand<Void, P> {
  private static final List<CommandArgumentDescriptor> argumentDescriptors = new ArrayList<>();
  static {
    argumentDescriptors.add(new CommandArgumentDescriptor("key", true));
  }

  @Override
  public Iterable<CommandArgumentDescriptor> getArgumentDescriptors() {
    return argumentDescriptors;
  }

  @Override
  public InsertCommand<P> getActualCommand(Map<String, CommandArgument> arguments, InputStream in, PrintStream out,
      PrintStream err) {
        return new InsertCommand<P>(null);
      }

    // receive streams
    // get collection we operating on
    // on that collection get element prototype
    // for each field of element prototype:
    // if field contains children
    // ...
    // if not, retrieve field's metadata.
    // with that metadata (field name especially) ask for input
    // how prompt knows witch type to return?
    // We create driver for that prompt, witch maps known types to prompt calls
}
