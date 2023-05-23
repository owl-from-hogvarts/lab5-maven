package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.TerminalUnavailable;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CommandQueue;

@NonNullByDefault
public class CliDependencyManager<CM extends ICollectionManager<?, ?>> {
  private final CM collectionManager;
  private final Map<String, ICliCommand<? super CliDependencyManager<CM>>> commands;
  private final LineReader lineReader;
  private final Streams streams;
  private final CommandQueue commandQueue = new CommandQueue();

  public CM getCollectionManager() {
    return collectionManager;
  }

  public Map<String, ICliCommand<? super CliDependencyManager<CM>>> getCommands() {
    return this.commands;
  }

  public CommandQueue getCommandQueue() {
    return this.commandQueue;
  }

  public LineReader getLineReader() {
    return this.lineReader;
  }

  public Streams getStreams() {
    return this.streams;
  }

  public CliDependencyManager(Builder<CM> builder)
      throws TerminalUnavailable {
    this.collectionManager = builder.collectionManager;
    this.commands = builder.commands;
    this.streams = builder.streams;

    try {
      final Terminal defaultTerminal = TerminalBuilder.builder().system(true).streams(streams.in, streams.out).build();
      this.lineReader = new LineReaderImpl(defaultTerminal);
    } catch (IOException e) {
      throw new TerminalUnavailable();
    }
  }

  public static class Builder<CM extends ICollectionManager<?, ?>> {
    private CM collectionManager;
    private Map<String, ICliCommand<? super CliDependencyManager<CM>>> commands;
    private Streams streams;
    public Builder<CM> setCollectionManager(CM collectionManager) {
      this.collectionManager = collectionManager;
      return this;
    }
    public Builder<CM> setCommands(Map<String, ICliCommand<? super CliDependencyManager<CM>>> commands) {
      this.commands = commands;
      return this;
    }
    public Builder<CM> setStreams(Streams streams) {
      this.streams = streams;
      return this;
    }
  }
}
