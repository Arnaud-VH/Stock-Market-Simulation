package nl.rug.aoop.networking.Server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main to instantiate the server and use it.
 */
@Slf4j
public class ServerMainSetUp {

    /**
     * Main.
     * @param args Console arguments.
     */
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Server server = new Server(6200);
            executorService.submit(server);
            log.info("Started the server at port: " + server.getPort());
        } catch (IOException e) {
            log.error("Could not start the server", e);
        }
    }
}
