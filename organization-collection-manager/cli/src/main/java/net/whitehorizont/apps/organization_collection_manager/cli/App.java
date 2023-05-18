package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.IOException;
import java.util.HashMap;
import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.organization_collection_manager.cli.commands.ICliCommand;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Insert;
import net.whitehorizont.apps.organization_collection_manager.cli.commands.Show;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.IncorrectNumberOfArguments;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.TerminalUnavailable;
import net.whitehorizont.apps.organization_collection_manager.cli.errors.UnknownCommand;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.CollectionMetadata;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollection;
import net.whitehorizont.apps.organization_collection_manager.core.collection.ICollectionManager;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElementFactory;
import net.whitehorizont.apps.organization_collection_manager.core.collection.OrganisationElement.OrganisationElementPrototype;
import net.whitehorizont.apps.organization_collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter.CollectionAdapter;

/**
 * Hello world!
 *
 */
@NonNullByDefault
public class App 
{
    public static void main( String[] args ) throws IOException, IncorrectNumberOfArguments, UnknownCommand, TerminalUnavailable
    {
        final var organisationElementFactory = new OrganisationElementFactory();
        final var xmlCollectionAdapter = new CollectionAdapter<>(organisationElementFactory);
        final var testStorage = new FileStorage<>("./test.xml", xmlCollectionAdapter);
        final var collectionManager = new CollectionManager<>(testStorage);
        final var commands = new HashMap<String, ICliCommand<? super CliDependencyManager<ICollectionManager<ICollection<OrganisationElementPrototype, OrganisationElement, CollectionMetadata>, CollectionMetadata>>>>();
        final var insert = new Insert<OrganisationElementPrototype, ICollectionManager<ICollection<OrganisationElementPrototype, OrganisationElement, CollectionMetadata>, CollectionMetadata>>();
        commands.put("insert", insert);
        final var show = new Show();
        commands.put("show", show);
        final var streams = new Streams(System.in, System.out, System.err);
        final var dependencyManager = new CliDependencyManager<ICollectionManager<ICollection<OrganisationElementPrototype, OrganisationElement, CollectionMetadata>, CollectionMetadata>>(collectionManager, commands, streams);
        final var cli = new CLI<>(dependencyManager);

        cli.start();
    }
}
