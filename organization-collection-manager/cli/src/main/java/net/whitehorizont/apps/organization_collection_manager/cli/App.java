package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Insert;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.commands.CommandQueue;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;

/**
 * Hello world!
 *
 */
@NonNullByDefault
public class App 
{
    public static void main( String[] args ) throws IOException, IncorrectNumberOfArguments, UnknownCommand
    {
        final HashMap<String, ICliCommand<?, ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>>> commands = new HashMap<String, ICliCommand<?, ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>>>();
        final ICliCommand<?, ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>> insert = new Insert<OrganisationElementPrototype, ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>>();
        commands.put("insert", insert);
        final OrganisationElementFactory organisationElementFactory = new OrganisationElementFactory();
        final var xmlCollectionAdapter = new CollectionAdapter<>(organisationElementFactory);
        final var testStorage = new FileStorage<>("./test.xml", xmlCollectionAdapter);
        final CollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata> collectionManager = new CollectionManager<>(testStorage);    
        final CliDependencyManager<ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>> dependencyManager = new CliDependencyManager<>(collectionManager);
        final Greeter<ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>> greeter = new Greeter<ICollectionManager<RamCollection<OrganisationElementPrototype, OrganisationElement>, CollectionMetadata>>(dependencyManager, commands, System.in, System.out, System.err);
        final var commandQueue = new CommandQueue();
        new CLI(greeter, commandQueue).start();
    }
}
