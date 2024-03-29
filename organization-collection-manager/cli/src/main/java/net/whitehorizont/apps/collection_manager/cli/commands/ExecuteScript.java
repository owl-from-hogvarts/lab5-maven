package net.whitehorizont.apps.collection_manager.cli.commands;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CLI;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.cli.Streams;
import net.whitehorizont.apps.collection_manager.cli.errors.RecursionDetected;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideAuthReceiver;
import net.whitehorizont.libs.file_system.PathHelpers;

@NonNullByDefault
public class ExecuteScript<DP extends IProvideAuthReceiver> implements ICliCommand<DP> {
  public static final String EXECUTE_SCRIPT_COMMAND = "execute_script";
  private static final String DESCRIPTION = "executes new line separated sequence of commands from file";
  private final Set<Path> runningScripts = new HashSet<>();
  private final Map<String, ICliCommand<? super DP>> executeScriptCommandSet;

  public ExecuteScript(Map<String, ICliCommand<? super DP>> executeScriptCommandSet) {
    this.executeScriptCommandSet = executeScriptCommandSet;
  }

  @Override
  public Optional<String> getArgumentName() {
    return Optional.of("path");
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends DP> dependencyManager, Stack<String> arguments)
      throws Exception {
    return Observable.create((subscriber) -> {
      // turn argument into resolved path
      final Path path = Path.of(arguments.pop());
      final var resolvedPath = PathHelpers.resolve(path);

      if (runningScripts.contains(resolvedPath)) {
        throw new RecursionDetected();
      }

      runningScripts.add(resolvedPath);

      final var file = resolvedPath.toFile();
      final InputStream fileInput = new FileInputStream(file);

      final var scriptStreams = new Streams(fileInput, dependencyManager.getStreams().out, dependencyManager.getStreams().err);
      // leave err untouched since we wan't to report errors into console
      // construct new cli instance with new stream configuration      
      final var executeScriptDependenciesConfig = new CliDependencyManager.Builder<DP>()
          .setStreams(scriptStreams)
          .setCommands(executeScriptCommandSet)
          .setOnInterruptHandler(() -> Observable.empty())
          .setCredentialManager(dependencyManager.getCredentialManager())
          .setGlobalErrorHandler((e, _dependencyManager) -> {
            dependencyManager.getGlobalErrorHandler().handle(e, _dependencyManager);
            return true;
          })
          .setCommandQueue((ICommandQueue<DP>) dependencyManager.getCommandQueue())
          .setDisplayPrompts(false)
          .setSystemTerminal(false);
      final var executeScriptDependencies = new CliDependencyManager<>(executeScriptDependenciesConfig);
      final var scriptCli = new CLI<>(executeScriptDependencies);
      try {
        scriptCli.start();
      } finally {
        fileInput.close();
        runningScripts.remove(resolvedPath);
        subscriber.onComplete();
      }
    });
  }

}
