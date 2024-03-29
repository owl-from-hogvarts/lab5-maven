package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideNothing;

@NonNullByDefault
public class Exit implements ICliCommand<IProvideNothing> {
  public static final String EXIT_COMMAND = "exit";
  private static final String DESCRIPTION = "Exit the program";
  private static final String EXIT_MESSAGE = "Exiting! All changes are saved by server";

  @Override
  public Optional<String> getArgumentName() {
    return Optional.empty();
  }
  

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends IProvideNothing> dependencyManager, Stack<String> arguments) {
    final var lineReader = dependencyManager.getCommandLineReader();
    final var output = lineReader.getTerminal().writer();
    output.println(EXIT_MESSAGE);
    output.flush();

    // final var exit = new ExitCommand();
    // dependencyManager.getCommandQueue().push(exit).blockingSubscribe();
    // lol who cares 😉
    System.exit(0);
    return Observable.empty();
  }
  
}
