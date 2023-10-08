package nl.rug.aoop.networking.Server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientHandler class that handles the connection of clients to the server and does the communication.
 */
@Slf4j
public class ClientHandler implements Runnable{
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running = false;
    private final int id;
    private final MessageHandler messageHandler;

    /**
     * Constructor for the clientHandler.
     * @param socket The socket through which communication with client and server happens.
     * @param id The ID of the client that is communicating with the server.
     * @param messageHandler The messageHandler instance that deals with client's messages.
     * @throws IOException The exception that results from the socket input and output stream.
     */
    public ClientHandler(Socket socket, int id, MessageHandler messageHandler) throws IOException {
        this.socket = socket;
        this.id = id;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        running = true;
        out.println("Hello, enter BYE to exit. Your ID is: " + id);
        while(running) {
            try {
                String fromClient = in.readLine();
                if (fromClient == null || fromClient.trim().equalsIgnoreCase("BYE")) {
                    terminate();
                    break;
                }
                log.info("Received message" + fromClient);
                out.println(fromClient);
                log.info("Received from client: " + id + fromClient);
            } catch (IOException e) {
                log.error("Reading string from client id: " + id, e);
            }
        }
    }

    /**
     * Terminates the connection over the socket.
     */
    public void terminate() {
        running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket", e);
        }
    }
}
