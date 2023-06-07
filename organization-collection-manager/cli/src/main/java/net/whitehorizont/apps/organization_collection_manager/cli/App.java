package net.whitehorizont.apps.organization_collection_manager.cli;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.Clear;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.CountByType;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.ExecuteScript;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Exit;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Help;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Info;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Insert;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.RemoveById;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.RemoveGreater;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.RemoveLower;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Save;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Show;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Update;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;

/**
 * Hello world!
 *
 */
@NonNullByDefault
public class App 
{
    public static void main( String[] args ) throws Throwable
    {
        final var testStorage = setupStorage("./test.xml");
        final var collectionManager = new CollectionManager<>(testStorage);

        final var commands = buildMainCommandSet();
        addSystemCommands(commands);
    
        // other configuration 
        final var streams = new Streams(System.in, System.out, System.err);
        final var collection = collectionManager.getCollection().blockingFirst();
        final var collectionCommandReceiver = new OrganisationCollectionCommandReceiver(collection);
        final var dependencyManagerBuilder = new CliDependencyManager.Builder<OrganisationCollectionCommandReceiver>()
            .setStreams(streams)
            .setCollectionReceiver(collectionCommandReceiver)
            .setGlobalErrorHandler(CLI::defaultGlobalErrorHandler)
            .setCommands(commands)
            .setCollectionManager(collectionManager);
        final var dependencyManager = new CliDependencyManager<>(dependencyManagerBuilder);
        final var cli = new CLI<>(dependencyManager);

        cli.start();
    }

    public static FileStorage<ICollection<OrganisationElementPrototype, OrganisationElement>> setupStorage(String fileStoragePath) {
        final var organisationElementFactory = new OrganisationElementFactory();
        final var xmlCollectionAdapter = new CollectionAdapter<>(organisationElementFactory);
        final var testStorage = new FileStorage<>(fileStoragePath, xmlCollectionAdapter);
        return testStorage;
    }

    public static Map<String, ICliCommand<? super OrganisationCollectionCommandReceiver>> buildMainCommandSet() {
        final var baseCommands = buildBaseCommandSet();
        final Insert.Retries retries = new Insert.Retries();
        final var insert = new Insert(retries);
        baseCommands.put("insert", insert);
        final var update = new Update(retries);
        baseCommands.put("update", update);

        final var executeScriptCommandSet = buildExecuteScriptCommandSet();
        final var executeScript = new ExecuteScript<>(executeScriptCommandSet);
        baseCommands.put(ExecuteScript.EXECUTE_SCRIPT_COMMAND, executeScript);
        executeScriptCommandSet.put(ExecuteScript.EXECUTE_SCRIPT_COMMAND, executeScript);


        return baseCommands;
    }

    public static Map<String, ICliCommand<? super OrganisationCollectionCommandReceiver>> buildBaseCommandSet() {
        final var commands = new HashMap<String, ICliCommand<? super OrganisationCollectionCommandReceiver>>();
        final var show = new Show();
        commands.put("show", show);
        final var save = new Save();
        commands.put("save", save);
        final var clear = new Clear();
        commands.put("clear", clear);
        final var info = new Info();
        commands.put("info", info);
        final var count = new CountByType();
        commands.put("count_by_type", count);
        final var removeById = new RemoveById();
        commands.put("remove_by_id", removeById);
        final var removeLower = new RemoveLower();
        commands.put("remove_lower", removeLower);
        final var RemoveGreater = new RemoveGreater();
        commands.put("remove_greater", RemoveGreater);

        return commands;
    }
    public static Map<String, ICliCommand<? super OrganisationCollectionCommandReceiver>> buildExecuteScriptCommandSet() {
        final var commands = buildBaseCommandSet();
        final Insert.Retries retries = new Insert.Retries(1); // does not make sense to infinitely ask for right data from script
        final var insert = new Insert(retries);
        commands.put("insert", insert);
        final var update = new Update(retries);
        commands.put("update", update);

        return commands;
    }

    public static void addSystemCommands(Map<String, ICliCommand<? super OrganisationCollectionCommandReceiver>> commands) {
        commands.put(Exit.EXIT_COMMAND, new Exit());
        commands.put(Help.HELP_COMMAND, new Help());
    }
}
