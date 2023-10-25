package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.handlers.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server that clients can connect to.
 */
@Slf4j
public class Server implements Runnable{

    private ServerSocket serverSocket;
    @Getter private final Map<Integer, ClientHandler> clientHandlers = new HashMap<>();
    private final MessageHandler messageHandler;
    private ExecutorService executorService;

    private volatile boolean terminate = false;
    @Getter private volatile boolean running = false;
    private int id = 0;

    /**
     * Server constructor.
     * @param port The port on which the server is hosted.
     * @param messageHandler The message handler which deals with the client's messages.
     */
    public Server(int port, MessageHandler messageHandler){
        this.messageHandler = messageHandler;
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newCachedThreadPool();
        } catch(IOException e) {
            log.error("The socket was not opened correctly: ", e);
        }
    }

    @Override
    public void run() {
        running = true;
        log.info("Server running");
        while (!terminate) {
            try {
                Socket socket = this.serverSocket.accept();
                log.info("Server accepted new connection");
                ClientHandler clientHandler = new ClientHandler(socket, id, messageHandler);
                clientHandlers.put(id, clientHandler);
                this.executorService.submit(clientHandler);
                id++;
            } catch (SocketException e) {
                if (!terminate) {  // if terminate socket has been closed so exception is expected (not an error)
                    log.error("Socket error: ", e);
                }
            } catch (IOException e) {
                log.error("Error when running server: ",e);
            }
        }
        executorService.shutdown();
        running = false;
        log.info("Server terminated");
    }

    /**
     * Sends a message from the server to the client by calling the appropriate clientHandler.
     * @param id The ID of the client to which the server sends the message.
     * @param message The message that is being sent.
     */
    public void sendMessage(int id, String message) {
        try {
            getClientHandlers().get(id).sendMessage(message);
        } catch (NullPointerException e) {
            log.error("Couldn't send message because client '" + id + "' doesn't exist", e);
        } catch (ClassCastException e) {
            log.error("Couldn't send message because id is of wrong type: ", e);
        }
    }

    /**
     * Terminates and stops the threads that are running.
     */
    public void terminate() {
        this.terminate = true;
        try {
            serverSocket.close();   // closing the serverSocket ensures the server doesn't get stuck trying to accept a
            // new connection when terminating
        } catch (IOException e) {
            log.error("Failed to close server socket: ", e);
        }
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }
}
