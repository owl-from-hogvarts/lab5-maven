package net.whitehorizont.libs.network.serialize;

import java.io.*;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class SerializeManager {
    public byte[] serialize(Serializable command) {
        try (ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(command);
            return outputStream.toByteArray();
        } catch (IOException ignore) {
            // as ByteArrayOutputStream is used, it should not throw any io exceptions
            throw new RuntimeException(ignore);
        }
    }
    public Object deserialize(byte[] data) throws ClassNotFoundException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (IOException ignore) {
            // as ByteArrayInputStream is used, it should not throw any io exceptions
            throw new RuntimeException();
        }
    }
}
