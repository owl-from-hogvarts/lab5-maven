package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CLI;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.RecursionDetected;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.libs.file_system.PathHelpers;

@NonNullByDefault
public class ExecuteScript<CM extends ICollectionManager<?, ?>> implements ICliCommand<CliDependencyManager<CM>> {
  public static final String EXECUTE_SCRIPT_COMMAND = "execute-script";
  private static final String DESCRIPTION = "executes new line separated sequence of commands from file";
  private final Set<Path> runningScripts = new HashSet<>();
  private final Map<String, ICliCommand<? super CliDependencyManager<CM>>> executeScriptCommandSet;

  public ExecuteScript(Map<String, ICliCommand<? super CliDependencyManager<CM>>> executeScriptCommandSet) {
    this.executeScriptCommandSet = executeScriptCommandSet;
  }

  @Override
  public boolean hasArgument() {
    return true;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<CM> dependencyManager, Stack<String> arguments)
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

      // build streams object with out stream redirected into void
      // and input stream set up to read from file
      final var voidStream = new PrintStream(OutputStream.nullOutputStream());
      final var scriptStreams = new Streams(fileInput, voidStream, dependencyManager.getStreams().err);
      // leave err untouched since we wan't to report errors into console
      // construct new cli instance with new stream configuration      
      final var executeScriptDependenciesConfig = new CliDependencyManager.Builder<CM>()
          .setStreams(scriptStreams)
          .setCollectionManager(dependencyManager.getCollectionManager())
          .setCommands(executeScriptCommandSet)
          .setOnInterruptHandler(() -> Observable.empty())
          .setGlobalErrorHandler((e, _dependencyManager) -> {
            dependencyManager.getGlobalErrorHandler().handle(e, _dependencyManager);
            return true;
          })
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
