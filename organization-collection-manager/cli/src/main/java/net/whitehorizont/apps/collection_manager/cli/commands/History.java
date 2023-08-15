package net.whitehorizont.apps.collection_manager.cli.commands;

import java.util.Optional;
import java.util.Stack;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.cli.CliDependencyManager;
import net.whitehorizont.apps.collection_manager.core.dependencies.IProvideNothing;

@NonNullByDefault
public class History implements ICliCommand<IProvideNothing> {
  private static final int COUNT_DISPLAY = 11;
  private static final String DESCRIPTION = "display last " + COUNT_DISPLAY + " commands";

  @Override
  public Optional<String> getArgument() {
    return Optional.empty();
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  @Override
  public Observable<Void> run(CliDependencyManager<? extends IProvideNothing> dependencyManager,
      Stack<String> arguments) throws Exception {
        final var history = dependencyManager.getCommandLineReader().getHistory();
        final var historyIterator = history.reverseIterator();
        final Stack<org.jline.reader.History.Entry> lastHistoryItems = new Stack<>();

        if (historyIterator.hasNext()) {
          // skip latest item, because it is always history command
          historyIterator.next();
        }

        final var out = dependencyManager.getStreams().out;
        int processed = 0;
        while (historyIterator.hasNext() && processed < COUNT_DISPLAY) {
          lastHistoryItems.push(historyIterator.next());
          processed++;
        }

        while (lastHistoryItems.size() > 0) {
          out.println(lastHistoryItems.pop().line());
        }
        
        return Observable.empty();
      }
  
}
