package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.HashMap;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Insert;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.Builder;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CommandQueue;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, IncorrectNumberOfArguments, UnknownCommand
    {
        final var commands = new HashMap<String, ICliCommand<?, ?>>();
        final var insertAdapter = new OrganisationInsertAdapter();
        commands.put("insert", new Insert<>(insertAdapter));
        final OrganisationElementFactory organisationElementFactory = new OrganisationElementFactory();
        final var xmlCollectionAdapter = new CollectionAdapter<>(organisationElementFactory);
        final var testStorage = new FileStorage<>("./test.xml", xmlCollectionAdapter);
        final ICollectionManager<RamCollection<Builder, OrganisationElement>, CollectionMetadata> collectionManager = new CollectionManager<>(testStorage);    
        final var dependencyManager = new CliDependencyManager<>(collectionManager);
        final var greeter = new Greeter(dependencyManager, commands, System.in, System.out, System.err);
        final var commandQueue = new CommandQueue();
        new CLI(greeter, commandQueue).start();
    }
}
