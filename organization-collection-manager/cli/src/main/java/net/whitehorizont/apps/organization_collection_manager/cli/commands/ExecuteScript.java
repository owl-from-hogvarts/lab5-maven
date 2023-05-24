package net.whitehorizont.apps.organization_collection_manager.cli.commands;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jline.reader.EndOfFileException;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.organization_collection_manager.cli.CLI;
import net.whitehorizont.apps.organization_collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.organization_collection_manager.cli.Streams;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.libs.file_system.PathHelpers;

@NonNullByDefault
public class ExecuteScript<CM extends ICollectionManager<?, ?>> implements ICliCommand<CliDependencyManager<CM>> {
  private static final String DESCRIPTION = "executes new line separated sequence of commands from file";

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
      final var pathResolved = PathHelpers.resolve(path);
      final var file = pathResolved.toFile();
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
          .setCommands(dependencyManager.getCommands())
          .setOnInterruptHandler(() -> Observable.error(new EndOfFileException()))
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
        subscriber.onComplete();
      }
    });
  }

}
