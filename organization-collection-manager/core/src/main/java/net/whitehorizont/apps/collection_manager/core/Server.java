package net.whitehorizont.apps.collection_manager.core;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNullByDefault;

import net.whitehorizont.apps.collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.collection_manager.core.collection.interfaces.ICollection;
import net.whitehorizont.apps.collection_manager.core.commands.CollectionManagerReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.CommandQueue;
import net.whitehorizont.apps.collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.SaveCommand;
import net.whitehorizont.apps.collection_manager.core.dependencies.CoreDependencyManager;
import net.whitehorizont.apps.collection_manager.core.storage.FileStorage;
import net.whitehorizont.apps.collection_manager.core.storage.collection_adapter.CollectionAdapter;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.organization_collection_manager.lib.ICanRichValidate;
import net.whitehorizont.apps.organization_collection_manager.lib.validators.ValidationError;
import net.whitehorizont.libs.network.past.Past;
import net.whitehorizont.libs.network.transport.udp.datagram_channel.DatagramChannelAdapter;

@NonNullByDefault
public class Server {
  public static void main(String[] args) throws StorageInaccessibleError {
    startServer().start();
  }

  public static CommandQueue<CoreDependencyManager<OrganisationCollectionCommandReceiver, OrganisationElementFull>, InetSocketAddress> startServer() throws StorageInaccessibleError {
    final var testStorage = setupStorage(getStoragePath());
    final var collectionManager = new CollectionManager<>(testStorage);
    final var collectionManagerReceiver = new CollectionManagerReceiver<>(collectionManager);

    final var collection = collectionManager.getCollection().blockingFirst();
    final var collectionCommandReceiver = new OrganisationCollectionCommandReceiver(collection);

    final var dependencyManager = new CoreDependencyManager<>(collectionCommandReceiver, collectionManagerReceiver);
    final CommandQueue<CoreDependencyManager<OrganisationCollectionCommandReceiver, OrganisationElementFull>, InetSocketAddress> commandQueue = new CommandQueue<>(dependencyManager, new Past<>(new DatagramChannelAdapter(new InetSocketAddress("localhost", 55555))));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      commandQueue.pushServer(new SaveCommand()).blockingSubscribe();
    }));

    return commandQueue;
  }

  private static FileStorage<ICollection<OrganisationElementFull>> setupStorage(String fileStoragePath) {
    final var collectionValidators = new ArrayList<ICanRichValidate<OrganisationElementFull, ICollection<OrganisationElementFull>>>();
    collectionValidators.add((element, collection) -> {
      final var idGetter = OrganisationElementDefinition.ID_METADATA.getValueGetter();
      final var duplicateIdElements$ = collection.getEvery$()
          .filter(e -> idGetter.apply(e).equals(idGetter.apply(element)));
      final var duplicateIdCount = duplicateIdElements$.count().blockingGet();
      if (duplicateIdCount > 0) {
        throw new ValidationError("Duplicate ID's found! ID should be unique!");
      }

      // TODO: handle error, regenerate id
    });

    final var xmlCollectionAdapter = new CollectionAdapter<OrganisationElementFull>(
        OrganisationElementDefinition.getMetadata());
    final var testStorage = new FileStorage<>(fileStoragePath, xmlCollectionAdapter);
    return testStorage;
  }

  private static String getStoragePath() {
    final String variableName = "collection_path";
    final String maybePath = System.getenv(variableName);
    if (maybePath == null) {
      throw new Error("Env variable " + variableName + " is not defined!");
    }

    return maybePath;
  }
}
