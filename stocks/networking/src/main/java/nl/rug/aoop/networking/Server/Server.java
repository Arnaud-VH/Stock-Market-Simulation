package nl.rug.aoop.networking.Server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server that clients can connect to.
 */
@Slf4j
public class Server implements Runnable{

    private final int port;
    private final ServerSocket serverSocket;
    @Getter private boolean running = false;
    @Getter private final boolean started;
    private int id = 0;
    private final ExecutorService executorService;
    private final MessageHandler messageHandler;

    /**
     * Server constructor.
     * @param port The port on which the server is hosted.
     * @param messageHandler The message handler which deals with the client's messages.
     * @throws IOException when the socket is opened.
     */
    public Server(int port, MessageHandler messageHandler) throws IOException {
        serverSocket = new ServerSocket(port);
        this.messageHandler = messageHandler;
        this.port = port;
        executorService = Executors.newCachedThreadPool();
        started = true;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket socket = this.serverSocket.accept();
                log.info("New connection from client");
                ClientHandler clientHandler = new ClientHandler(socket, id, messageHandler);
                this.executorService.submit(clientHandler);
                id++;
            } catch (IOException e) {
                log.error("Socket error: ", e);
            }
        }
    }

    /**
     * Terminates and stops the threads that are running.
     */
    public void terminate() {
        running = false;
        this.executorService.shutdown();
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }
}
