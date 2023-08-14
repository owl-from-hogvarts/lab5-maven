package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.libs.file_system.StringHelper;

@NonNullByDefault
public class Help implements ICliCommand<CollectionCommandReceiver<?>> {

  private static final String DESCRIPTION = "prints this help message";
  private static final int INDENT_SIZE = 2;
  private static final String INDENT_SYMBOL = " ";
  private static final String INDENT = INDENT_SYMBOL.repeat(INDENT_SIZE);
  private static final String DESCRIPTION_SEPARATOR = "-";
  private static final String WORD_SEPARATOR = " ";
  public static final String HELP_COMMAND = "help";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<?> dependencyManager, Stack<String> arguments) {
    return Observable.create(subscriber -> {
      final var commandDescriptions = new ArrayList<Pair<String, String>>();
      final Map<String, ? extends ICliCommand<?>> commands = dependencyManager
          .getCommands();
      for (final var command : commands.entrySet()) {
        final var commandDescription = retrieveCommandDescription(command);
        commandDescriptions.add(commandDescription);
      }

      final var lineReader = dependencyManager.getCommandLineReader();
      final var output = lineReader.getTerminal().writer();
      final var maxCommandNameLength = commandDescriptions.stream().map(command -> command.getValue0().length())
          .max(Integer::compare).get().intValue();
      for (final var commandDescription : commandDescriptions) {
        final var commandNamePadded = StringHelper.padStart(commandDescription.getValue0(), maxCommandNameLength, WORD_SEPARATOR);
        final var descriptionArray = new ArrayList<String>();
        descriptionArray.add(commandNamePadded);
        descriptionArray.add(DESCRIPTION_SEPARATOR);
        descriptionArray.add(commandDescription.getValue1());

        final var description = String.join(WORD_SEPARATOR, descriptionArray);
        final var descriptionIndented = INDENT + description;

        output.println(descriptionIndented);
      }
      subscriber.onComplete();
    });
  }

  private Pair<String, String> retrieveCommandDescription(
      Entry<String, ? extends ICliCommand<?>> command) {
        String argument = "";
        if (command.getValue().getArgument().isPresent()) {
          argument = " {" + command.getValue().getArgument().get() + "}";
        }
    return new Pair<String, String>(command.getKey() + argument, command.getValue().getCommandDescription());
  }

}