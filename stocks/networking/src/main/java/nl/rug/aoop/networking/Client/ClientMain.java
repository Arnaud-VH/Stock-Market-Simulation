package nl.rug.aoop.networking.Client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main where we can instantiate the client and see if it runs with the server.
 */
@Slf4j
public class ClientMain {

    /**
     * Main for the client side.
     * @param args Console arguments.
     */
    public static void main(String[] args) {
        MessageLogger messageLogger = new MessageLogger();
        try {
            Client client = new Client(new InetSocketAddress("localhost", 6200), messageLogger);
            //Should we also use the runnable interface here and submit it to an executor service?
            client.run();
            log.info("Successfully connected to the server ");

        } catch (IOException e) {
            log.error("Client could not connect to server");
        }
    }
}
