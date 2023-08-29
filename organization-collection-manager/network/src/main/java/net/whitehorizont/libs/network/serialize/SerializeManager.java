package net.whitehorizont.libs.network.serialize;

import net.whitehorizont.apps.collection_manager.core.commands.interfaces.ICommand;

import java.io.*;

public class SerializeManager {
    public byte[] serialize(ICommand command) throws IOException {
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(command);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }
    public ICommand deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ICommand command = (ICommand) objectInputStream.readObject();
        objectInputStream.close();
        return command;
    }
}
