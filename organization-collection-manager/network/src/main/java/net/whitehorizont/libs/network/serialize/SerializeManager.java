package net.whitehorizont.libs.network.serialize;

import java.io.*;

public class SerializeManager {
    public byte[] serialize(Serializable command) throws IOException {
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(command);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }
    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }
}
