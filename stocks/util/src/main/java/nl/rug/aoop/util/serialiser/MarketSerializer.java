package nl.rug.aoop.util.serialiser;

import java.io.*;
import java.util.Base64;

/**
 * Class to serialize to and from string which is useful to communicate over network.
 */
public class MarketSerializer {
    /**
     * Deserializes string to specified object type.
     * @param s String to deserialize
     * @param type Class of object to deserialize to
     * @param <T> Class of object to deserialize to
     * @return Object of type 'type'.
     * @throws IOException If IOException is thrown when deserializing
     * @throws ClassNotFoundException If ClassNotFoundException is thrown when deserializing
     */
    public static <T extends Serializable> T fromString(String s, Class<T> type) throws IOException,
            ClassNotFoundException {
        byte[] byte_Data = Base64.getDecoder().decode(s);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byte_Data));
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        return type.cast(o);
    }

    /**
     * Serializes object to a string.
     * @param object Object to serialize
     * @return String representing serialized object
     * @throws IOException Thrown when failed to serialize
     */
    public static String toString(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
}
