package nl.rug.aoop.networking.Client;

/**
 * Interface that dictates the behaviour of message handler for communication between client and server.
 */
public interface MessageHandler {

    /**
     * Performs the appropriate handling of the message.
     * @param message The String message sent from the server.
     */
    void handleMessage(String message);
}
