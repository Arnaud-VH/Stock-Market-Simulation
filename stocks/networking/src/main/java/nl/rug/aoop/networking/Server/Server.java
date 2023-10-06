package nl.rug.aoop.networking.Server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Client.MessageHandler;
import nl.rug.aoop.networking.Command.CommandHandler;

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
    private ServerSocket serverSocket;
    @Getter private boolean running = false;
    @Getter private boolean started;
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
        executorService = Executors.newCachedThreadPool(); //Don't want to limit ourselves
        started = true;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                //Socket is the form of communication with the client and the server.
                Socket socket = this.serverSocket.accept(); //Stuck until new connection
                //New connection with the server
                log.info("New connection from client");
                //Here we handle the new client connections, with a new class.
                ClientHandler clientHandler = new ClientHandler(socket, id, messageHandler);
                this.executorService.submit(clientHandler); //Takes a runnable, which is the clientHandler.
                id++; //Increment the ID everytime a new client connects.
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
        this.executorService.shutdown(); //All clientHandler's in thread pool to shutdown
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }
}
