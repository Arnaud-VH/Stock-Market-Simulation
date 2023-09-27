package nl.rug.aoop.networking.Server;

import lombok.extern.slf4j.Slf4j;

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
    private boolean running = false;
    private int id = 0;
    private ExecutorService executorService;

    /**
     * Server constructor.
     * @param port The port on which the server is hosted.
     * @throws IOException when the socket is opened.
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
        executorService = Executors.newCachedThreadPool(); //Don't want to limit outrselves
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
                ClientHandler clientHandler = new ClientHandler(socket, id);
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

    public boolean isRunning() {
        return this.running;
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }
}
