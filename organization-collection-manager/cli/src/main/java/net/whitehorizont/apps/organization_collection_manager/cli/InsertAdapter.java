package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;

@NonNullByDefault
public interface InsertAdapter<P> {
  InsertCommand<P> getCommand(Stack<String> arguments, InputStream in, OutputStream out);
}
