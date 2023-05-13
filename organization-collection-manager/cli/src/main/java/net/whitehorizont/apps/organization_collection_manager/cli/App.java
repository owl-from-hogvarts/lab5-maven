package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.HashMap;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CommandQueue;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, IncorrectNumberOfArguments, UnknownCommand
    {
        final var commands = new HashMap<String, ICliCommand<?>>();
        final var greeter = new Greeter(commands, System.in, System.out, System.err);
        final var commandQueue = new CommandQueue();
        new CLI(greeter, commandQueue).start();
    }
}
