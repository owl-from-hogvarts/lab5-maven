package net.whitehorizont.apps.collection_manager.cli;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.cli.commands.Clear;
import net.whitehorizont.apps.collection_manager.cli.commands.CountByType;
import net.whitehorizont.apps.collection_manager.cli.commands.ExecuteScript;
import net.whitehorizont.apps.collection_manager.cli.commands.Exit;
import net.whitehorizont.apps.collection_manager.cli.commands.FilterStartsWith;
import net.whitehorizont.apps.collection_manager.cli.commands.Help;
import net.whitehorizont.apps.collection_manager.cli.commands.History;
import net.whitehorizont.apps.collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.collection_manager.cli.commands.Info;
import net.whitehorizont.apps.collection_manager.cli.commands.Insert;
import net.whitehorizont.apps.collection_manager.cli.commands.PrintDescending;
import net.whitehorizont.apps.collection_manager.cli.commands.RemoveById;
import net.whitehorizont.apps.collection_manager.cli.commands.RemoveGreater;
import net.whitehorizont.apps.collection_manager.cli.commands.RemoveLower;
import net.whitehorizont.apps.collection_manager.cli.commands.Show;
import net.whitehorizont.apps.collection_manager.cli.commands.Update;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionMetadataDefinition;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommandQueue;
import net.whitehorizont.apps.collection_manager.core.dependencies.IUniversalCoreProvider;
import net.whitehorizont.apps.collection_manager.organisation.commands.IOrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.libs.network.past.Past;
import net.whitehorizont.libs.network.transport.udp.datagram_channel.DatagramChannelAdapter;

/**
 * Hello world!
 *
 */
@NonNullByDefault
public class App 
{
    static {
        Thread.setDefaultUncaughtExceptionHandler(CLI::defaultGlobalErrorHandler);
    }

    public static void main( String[] args ) throws Throwable
    {


        final var commands = buildMainCommandSet();
        addSystemCommands(commands);
    
        final var connectionToServer = new Past<>(new DatagramChannelAdapter(new InetSocketAddress("localhost", 0))).connect(new InetSocketAddress("localhost", 55555));
        final ICommandQueue<IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver, OrganisationElementFull>> commandQueue = new NetworkCommandQueue<>(connectionToServer);

        // other configuration 
        final var streams = new Streams(System.in, System.out, System.err);
        final var dependencyManagerBuilder = new CliDependencyManager.Builder<IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver, OrganisationElementFull>>()
            .setStreams(streams)
            .setGlobalErrorHandler(CLI::defaultGlobalErrorHandler)
            .setCommands(commands)
            .setCommandQueue(commandQueue);
        final var dependencyManager = new CliDependencyManager<>(dependencyManagerBuilder);
        final var cli = new CLI<>(dependencyManager);

        cli.start();
    }



    public static Map<String, ICliCommand<? super IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver, OrganisationElementFull>>> buildMainCommandSet() {
        final var baseCommands = buildBaseCommandSet();
        final Insert.Retries retries = new Insert.Retries();
        final var insert = new Insert(OrganisationElementDefinition.getInputMetadata(), new OrganisationElementDefinition.OrganisationElementFactory(), retries);
        baseCommands.put("insert", insert);
        final var update = new Update(OrganisationElementDefinition.getInputMetadata(), new OrganisationElementDefinition.OrganisationElementFactory(), retries);
        baseCommands.put("update", update);

        final var executeScriptCommandSet = buildExecuteScriptCommandSet();
        final var executeScript = new ExecuteScript<>(executeScriptCommandSet);
        baseCommands.put(ExecuteScript.EXECUTE_SCRIPT_COMMAND, executeScript);
        executeScriptCommandSet.put(ExecuteScript.EXECUTE_SCRIPT_COMMAND, executeScript);


        return baseCommands;
    }

    public static Map<String, ICliCommand<? super IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver, OrganisationElementFull>>> buildBaseCommandSet() {
        final var commands = new HashMap<String, ICliCommand<? super IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver, OrganisationElementFull>>>();
        final var show = new Show<>(OrganisationElementDefinition.getMetadata());
        commands.put("show", show);
        // final var save = new Save();
        // commands.put("save", save);
        final var clear = new Clear();
        commands.put("clear", clear);
        final var info = new Info(CollectionMetadataDefinition.getMetadata());
        commands.put("info", info);
        final var count = new CountByType();
        commands.put("count_by_type", count);
        final var removeById = new RemoveById();
        commands.put("remove_by_id", removeById);
        final var removeLower = new RemoveLower();
        commands.put("remove_lower", removeLower);
        final var RemoveGreater = new RemoveGreater();
        commands.put("remove_greater", RemoveGreater);
        final var filterStartsWith = new FilterStartsWith(OrganisationElementDefinition.getMetadata());
        commands.put("filter_starts_with_full_name", filterStartsWith);
        final var history = new History();
        commands.put("history", history);
        final var printDescending = new PrintDescending(OrganisationElementDefinition.getMetadata());
        commands.put("print_descending", printDescending);

        return commands;
    }
    public static Map<String, ICliCommand<? super IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver,OrganisationElementFull>>> buildExecuteScriptCommandSet() {
        final var commands = buildBaseCommandSet();
        final Insert.Retries retries = new Insert.Retries(1); // does not make sense to infinitely ask for right data from script
        final var insert = new Insert(OrganisationElementDefinition.getInputMetadata(), new OrganisationElementDefinition.OrganisationElementFactory(), retries);
        commands.put("insert", insert);
        final var update = new Update(OrganisationElementDefinition.getInputMetadata(), new OrganisationElementDefinition.OrganisationElementFactory(), retries);
        commands.put("update", update);

        return commands;
    }

    public static void addSystemCommands(Map<String, ICliCommand<? super IUniversalCoreProvider<? extends IOrganisationCollectionCommandReceiver,OrganisationElementFull>>> commands) {
        commands.put(Exit.EXIT_COMMAND, new Exit());
        commands.put(Help.HELP_COMMAND, new Help());
    }
}
