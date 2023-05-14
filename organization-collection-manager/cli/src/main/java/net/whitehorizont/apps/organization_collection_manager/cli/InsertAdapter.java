package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;

import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;
import net.whitehorizont.apps.organization_collection_manager.core.storage.errors.StorageInaccessibleError;

@NonNullByDefault
public interface InsertAdapter<P> {
  InsertCommand<P> getCommand(ICollectionManager<ICollection<P, ?, ?>, ?> CollectionManager, Stack<String> arguments, LineReader lineReader) throws IOException, StorageInaccessibleError;
}
