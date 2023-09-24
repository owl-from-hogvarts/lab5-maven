package net.whitehorizont.apps.collection_manager.core.commands;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import io.reactivex.rxjava3.core.Observable;
import net.whitehorizont.apps.collection_manager.core.commands.errors.IncorrectPassword;
import net.whitehorizont.apps.collection_manager.core.commands.errors.NoSuchUser;
import net.whitehorizont.apps.collection_manager.core.commands.interfaces.IAuthReceiver;
import net.whitehorizont.apps.collection_manager.core.crypto.ICryptoProvider;
import net.whitehorizont.apps.collection_manager.core.storage.DatabaseConnectionFactory;
import net.whitehorizont.apps.collection_manager.core.storage.SqlUtils;
import net.whitehorizont.apps.collection_manager.core.storage.errors.StorageInaccessibleError;

public class LoginPasswordAuthReceiver implements IAuthReceiver {
  private final DatabaseConnectionFactory connectionFactory;

  private final ICryptoProvider crypto;
  private final Random random = new Random();

  public LoginPasswordAuthReceiver(ICryptoProvider crypto, DatabaseConnectionFactory connectionFactory) {
    this.crypto = crypto;
    this.connectionFactory = connectionFactory;
  }

  @Override
  public void register(String login, String password) throws StorageInaccessibleError {

    final var hashedPassword = hashPassword(password);
    // save record into database
    SqlUtils.safeExecuteQuery(connectionFactory, "INSERT INTO users VALUES (?, ?, ?)", statement -> {
      statement.setString(1, login);
      statement.setBytes(2, hashedPassword.passwordHash);
      statement.setBytes(3, hashedPassword.salt);
    }, null);
  }

  /**
   * Use to ping auth data
   * 
   * @throws StorageInaccessibleError
   */
  @Override
  public Observable<Void> login(String login, String password) {
    // check if user exists
    // check if password is corrects
    return Observable.create(subscriber -> {
      // select user with specified login from database
      SqlUtils.safeExecuteQuery(connectionFactory, "SELECT hashed_password, salt FROM users WHERE login = ?;",
          statement -> {
            statement.setString(1, login);
          }, resultSet -> {
            // if nothing found, return error
            if (!resultSet.next()) {
              subscriber.onError(new NoSuchUser(login));
            }
            // else
            // retrieve password_hash and salt
            final var password_hash_db = resultSet.getBytes("hashed_password");
            final var salt = resultSet.getBytes("salt");
            // compute password_hash with specified salt
            final byte[] password_hash_user = hashPassword(password, salt);
            // compare hash from db and computed one
            // if they match return nothing
            // if not, throw error
            final boolean isValid = Arrays.compare(password_hash_db, password_hash_user) == 0;
            if (!isValid) {
              subscriber.onError(new IncorrectPassword(login));
            }

            subscriber.onComplete();
          });
    });
  }

  private byte[] hashPassword(String password, byte[] salt) {
    final var hashedPassword = crypto.apply(password.getBytes(StandardCharsets.UTF_8));
    // add salt (xor operation)
    assert salt.length == hashedPassword.length;
    for (int i = 0; i < salt.length; i++) {
      hashedPassword[i] ^= salt[i];
    }

    // hash again
    return crypto.apply(hashedPassword);
  }

  private SaltedPassword hashPassword(String password) {
    // generate hash from password
    final byte[] salt = new byte[crypto.getHashLength()];
    random.nextBytes(salt);

    final var saltedHashedPassword = hashPassword(password, salt);
    return new SaltedPassword(saltedHashedPassword, salt);
  }

  private static class SaltedPassword {
    private final byte[] passwordHash;
    private final byte[] salt;

    private SaltedPassword(byte[] passwordHash, byte[] salt) {
      this.passwordHash = passwordHash;
      this.salt = salt;
    }
  }
}
