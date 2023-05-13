package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.cli.InsertAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;

@NonNullByDefault
public class Insert<P> implements ICliCommand<Void> {

  private static final int REQUIRED_ARGUMENTS_COUNT = 0;
  private static final int MAX_ARGUMENT_COUNT = 1;

  private final InsertAdapter<P> adapter;

  public Insert(InsertAdapter<P> adapter) {
    this.adapter = adapter;
  }

  @Override
  public int getRequiredArgumentsCount() {
    return REQUIRED_ARGUMENTS_COUNT;
  }

  @Override
  public InsertCommand<P> getActualCommand(Stack<String> arguments, InputStream in, OutputStream out) {
    return adapter.getCommand(arguments, in, out);
  }

  @Override
  public int getMaxArgumentCount() {
    return MAX_ARGUMENT_COUNT;
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
