package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.commands.InsertCommand;

@NonNullByDefault
public class OrganisationInsertAdapter implements InsertAdapter<Builder> {
  private final LineReader reader;

  public OrganisationInsertAdapter() {
    final Terminal defaultTerminal = TerminalBuilder.builder().system(false).streams(in, out).build(); 
    this.reader = new LineReaderImpl(defaultTerminal);

  }

  @Override
  public InsertCommand<Builder> getCommand(Stack<String> arguments, InputStream in,
      OutputStream out) {
        final var nameMetadata = OrganisationElement.getNameMetadata();
        final var name = reader.readLine(nameMetadata.getDisplayedName()).trim();

        final var elementBuilder = new Builder().name(name);

        

        new InsertCommand<>(elementBuilder, null)
      }
  
}
