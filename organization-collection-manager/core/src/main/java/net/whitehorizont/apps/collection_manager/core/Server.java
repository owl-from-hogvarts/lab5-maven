package net.whitehorizont.apps.collection_manager.core;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.electronwill.nightconfig.core.file.FileConfig;

import io.reactivex.rxjava3.annotations.NonNull;
import net.whitehorizont.apps.collection_manager.core.collection.CollectionManager;
import net.whitehorizont.apps.collection_manager.core.collection.RamCollection;
import net.whitehorizont.apps.collection_manager.core.collection.middleware.CollectionMiddleware;
import net.whitehorizont.apps.collection_manager.core.collection.middleware.DatabaseDelete;
import net.whitehorizont.apps.collection_manager.core.collection.middleware.DatabaseInsert;
import net.whitehorizont.apps.collection_manager.core.collection.middleware.ValidateMiddleware;
import net.whitehorizont.apps.collection_manager.core.commands.CollectionManagerReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.CommandQueue;
import net.whitehorizont.apps.collection_manager.core.commands.LoginPasswordAuthReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.OrganisationCollectionCommandReceiver;
import net.whitehorizont.apps.collection_manager.core.commands.SaveCommand;
import net.whitehorizont.apps.collection_manager.core.crypto.Sha256;
import net.whitehorizont.apps.collection_manager.core.dependencies.CoreDependencyManager;
import net.whitehorizont.apps.collection_manager.core.storage.DatabaseConnectionFactory;
import net.whitehorizont.apps.collection_manager.core.storage.DatabaseStorage;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFull;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFullFactory;
import net.whitehorizont.apps.collection_manager.organisation.definitions.OrganisationElementDefinition.OrganisationElementFullWritable;
import net.whitehorizont.libs.network.past.Past;
import net.whitehorizont.libs.network.transport.udp.datagram_channel.DatagramChannelAdapter;

@NonNullByDefault
public class Server {
  public static void main(String[] args) throws StorageInaccessibleError {
    startServer(args).start();
  }

  public static CommandQueue<CoreDependencyManager<OrganisationCollectionCommandReceiver, OrganisationElementFull>, InetSocketAddress> startServer(String[] args)
      throws StorageInaccessibleError {
    // final var testStorage = setupStorage(getStoragePath());
    final var connectionFactory = setupDatabaseConnectionFactory(args);

    final var testStorage = setupDatabase(connectionFactory);
    final var collectionManager = new CollectionManager<>(testStorage);
    final var collectionManagerReceiver = new CollectionManagerReceiver<>(collectionManager);

    final var collection = collectionManager.getCollection().blockingFirst();
    final var collectionCommandReceiver = new OrganisationCollectionCommandReceiver(collection);

    final var dependencyManager = new CoreDependencyManager<>(collectionCommandReceiver, collectionManagerReceiver, new LoginPasswordAuthReceiver(new Sha256(), connectionFactory));
    final CommandQueue<CoreDependencyManager<OrganisationCollectionCommandReceiver, OrganisationElementFull>, InetSocketAddress> commandQueue = new CommandQueue<>(
        dependencyManager, new Past<>(new DatagramChannelAdapter(new InetSocketAddress(57461))));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      commandQueue.pushServer(new SaveCommand()).blockingSubscribe();
    }));

    return commandQueue;
  }

  // private static FileStorage<ICollection<OrganisationElementFull>>
  // setupFileStorage(String fileStoragePath) {
  // final var collectionValidators = new
  // ArrayList<ICanRichValidate<OrganisationElementFull, ? super
  // ICollection<OrganisationElementFull>>>();
  // collectionValidators.add((element, collection) -> {
  // final var idGetter =
  // OrganisationElementDefinition.ID_METADATA.getValueGetter();
  // final var duplicateIdElements$ = collection.getEvery$()
  // .filter(e -> idGetter.apply(e).equals(idGetter.apply(element)));
  // final var duplicateIdCount = duplicateIdElements$.count().blockingGet();
  // if (duplicateIdCount > 0) {
  // throw new ValidationError("Duplicate ID's found! ID should be unique!");
  // }

  // // TODO: handle error, regenerate id
  // });

  // final var xmlCollectionAdapter = new
  // CollectionAdapter<OrganisationElementFull>();
  // final var testStorage = new FileStorage<>(fileStoragePath,
  // xmlCollectionAdapter);
  // return testStorage;
  // }

  private static String getStoragePath() {
    final String variableName = "collection_path";
    final String maybePath = System.getenv(variableName);
    if (maybePath == null) {
      throw new Error("Env variable " + variableName + " is not defined!");
    }

    return maybePath;
  }

  private static DatabaseStorage<OrganisationElementFull, OrganisationElementFullWritable> setupDatabase(DatabaseConnectionFactory connectionFactory) {
    final var elementMetadata = OrganisationElementDefinition.getMetadata();
    final RamCollection.Configuration<OrganisationElementFull, ?> collectionConfiguration = new RamCollection.Configuration<>();
    final List<CollectionMiddleware<OrganisationElementFull>> insertMiddleware = new ArrayList<>();


    final var validationMiddleware = new ValidateMiddleware<OrganisationElementFull>(elementMetadata); 
    insertMiddleware.add(validationMiddleware);

    collectionConfiguration.elementMetadata(elementMetadata)
        .insertMiddleware(insertMiddleware)
        .deleteMiddleware(new ArrayList<>());
    return new DatabaseStorage<>(connectionFactory, new OrganisationElementFullFactory(),
        elementMetadata, collectionConfiguration, new DatabaseInsert(connectionFactory) ,new DatabaseDelete(connectionFactory), "organisations");

  }

  private static DatabaseConnectionFactory setupDatabaseConnectionFactory(String args[]) {
    final String configPath = args.length > 0 ? args[0] : "./database.toml";
    final var config = FileConfig.of(configPath);
    config.load();
    final @NonNull String database_path = config.get("db_path");
    final @NonNull String database_user = config.get("user");
    final @NonNull String database_password = config.get("password");
    final var connectionFactory = new DatabaseConnectionFactory(database_path, database_user, database_password);
    return connectionFactory;
  }
}
