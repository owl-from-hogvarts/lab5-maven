package net.whitehorizont.apps.collection_manager.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import net.whitehorizont.apps.collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.collection_manager.cli.errors.IGlobalErrorHandler;
import net.whitehorizont.apps.collection_manager.cli.errors.IInterruptHandler;
import net.whitehorizont.apps.collection_manager.cli.errors.TerminalUnavailable;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;

@NonNullByDefault
public class CliDependencyManager<DP> {

  private final Map<String, ICliCommand<? super DP>> commands;
  private final LineReader commandLineReader;
  private final LineReader genericLineReader;
  private final boolean displayPrompts;

  public boolean getDisplayPrompts() {
    return displayPrompts;
  }

  /**
   * Use within commands
   * @return
   */
  public LineReader getGenericLineReader() {
    return genericLineReader;
  }

  private final Streams streams;
  private final ICommandQueue<DP> commandQueue;
  private final Optional<IInterruptHandler> onInterrupt;
  private final IGlobalErrorHandler globalErrorHandler;

  public IGlobalErrorHandler getGlobalErrorHandler() {
    return globalErrorHandler;
  }

  public Optional<IInterruptHandler> getOnInterrupt() {
    return onInterrupt;
  }

  public Map<String, ICliCommand<? super DP>> getCommands() {
    return this.commands;
  }

  public ICommandQueue<DP> getCommandQueue() {
    return this.commandQueue;
  }

  /**
   * Use to ask for commands
   * @return
   */
  public LineReader getCommandLineReader() {
    return this.commandLineReader;
  }

  public Streams getStreams() {
    return this.streams;
  }

  public CliDependencyManager(Builder<DP> builder)
      throws TerminalUnavailable {
    this.commands = builder.commands;
    this.streams = builder.streams;
    this.onInterrupt = Optional.ofNullable(builder.onInterruptHandler);
    this.globalErrorHandler = builder.globalErrorHandler;
    this.displayPrompts = builder.displayPrompts;
    this.commandQueue = builder.commandQueue;

    try {
      final PrintStream voidStream = new PrintStream(OutputStream.nullOutputStream());
      final TerminalBuilder terminalBuilder = TerminalBuilder.builder().streams(streams.in, builder.displayPrompts ? streams.out : voidStream);
      if (builder.isSystemTerminal) {
        terminalBuilder.system(true);
      } else {
        terminalBuilder.dumb(true);
      }

      final Terminal defaultTerminal = terminalBuilder.build();
      // final var attr = new Attributes();
      // attr.setInputFlag(Attributes.InputFlag.BRKINT, true);
      // defaultTerminal.setAttributes(attr);
      this.commandLineReader = LineReaderBuilder.builder().terminal(defaultTerminal).build();
      this.genericLineReader = LineReaderBuilder.builder().terminal(defaultTerminal).build();
    } catch (IOException e) {
      throw new TerminalUnavailable();
    }
  }

  public static class Builder<DP> {
    private Map<String, ICliCommand<? super DP>> commands;
    private Streams streams;
    private @Nullable IInterruptHandler onInterruptHandler;
    private boolean isSystemTerminal = true;
    private @Nullable IGlobalErrorHandler globalErrorHandler;
    private boolean displayPrompts = true;
    private ICommandQueue<DP> commandQueue;

    public Builder<DP> setCommandQueue(ICommandQueue<DP> commandQueue) {
      this.commandQueue = commandQueue;
      return this;
    }

    public Builder<DP> setDisplayPrompts(boolean displayPrompts) {
      this.displayPrompts = displayPrompts;
      return this;
    }
    /**
     * If error handler return true, this means that fatal error happened 
     * (or we just need to stop current instance of CLI without exiting program).
     * So returning true will just stop current instance of CLI
     * @param globalErrorHandler
     * @return
     */
    public Builder<DP> setGlobalErrorHandler(IGlobalErrorHandler globalErrorHandler) {
      this.globalErrorHandler = globalErrorHandler;
      return this;
    }
    public Builder<DP> setSystemTerminal(boolean isSystemTerminal) {
      this.isSystemTerminal = isSystemTerminal;
      return this;
    }
    public Builder<DP> setOnInterruptHandler(IInterruptHandler onInterruptHandler) {
      this.onInterruptHandler = onInterruptHandler;
      return this;
    }
    public Builder<DP> setCommands(Map<String, ICliCommand<? super DP>> commands) {
      this.commands = commands;
      return this;
    }
    public Builder<DP> setStreams(Streams streams) {
      this.streams = streams;
      return this;
    }
  }
}
