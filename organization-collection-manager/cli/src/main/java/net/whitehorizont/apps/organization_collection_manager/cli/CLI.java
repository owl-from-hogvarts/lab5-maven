package net.whitehorizont.apps.organization_collection_manager.cli;

import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CommandQueue;

public class CLI {
  private final CommandQueue commandQueue;
  private final Greeter greeter;
  private final CollectionCommandReceiver collectionReceiver;

  public CLI(Greeter greeter, CommandQueue commandQueue) {
    this.commandQueue = commandQueue;
    this.greeter = greeter;

    

    // listen on greeter for new cli commands
    // map them to actual commands
    // send actual commands to queue
  }

  public void start() throws IncorrectNumberOfArguments {
    while (true) {
      final var commandMaybe = greeter.promptCommand();
      if (commandMaybe.isEmpty()) {
        continue;
      }

      final var command = commandMaybe.get();
      commandQueue.push(command).subscribe();
    }
  }
}
