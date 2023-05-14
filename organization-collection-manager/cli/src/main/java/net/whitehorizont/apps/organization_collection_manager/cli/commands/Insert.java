package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;

import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.InsertAdapter;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public class Insert<P> implements ICliCommand<Void, ICollectionManager<ICollection<P, ?, ?>, ?>> {
  private static final String DESCRIPTION = "insert element into collection";

  private final InsertAdapter<P> adapter;

  public Insert(InsertAdapter<P> adapter) {
    this.adapter = adapter;
  }

  @Override
  public InsertCommand<P> getActualCommand(CliDependencyManager<ICollectionManager<ICollection<P, ?, ?>, ?>> dependencyManager, Stack<String> arguments, LineReader lineReader) throws IOException, StorageInaccessibleError {
    final var collectionManager = dependencyManager.getCollectionManager();
    return adapter.getCommand(collectionManager, arguments, lineReader);
  }

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
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
