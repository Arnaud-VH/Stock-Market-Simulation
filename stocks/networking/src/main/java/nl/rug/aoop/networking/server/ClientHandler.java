package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.handlers.MessageHandler;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;

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
    @Getter private volatile boolean running = false;
    private volatile boolean terminate = false;
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
        log.info("Client '" + id + "' is being handled");
        log.info("Sending client_id to client " + id);
        out.println(new NetworkMessage("client_id", String.valueOf(id)).toJson());  // send client's id to client
        while(!terminate) {
            try {
                String fromClient = in.readLine();
                if (fromClient == null) {  // force close connection if client sends null.
                    log.info("Terminating clientHandler '" + id + "' because client sent null");
                    break;
                }
                out.println(new NetworkMessage("echo",fromClient).toJson());  // echo client message
                messageHandler.handleMessage(fromClient);
            } catch (IOException e) {
                log.error("Error reading message from client '" + id + "': ", e);
            }
        }
        running = false;
        log.info("Client '" + id + "' disconnected");
    }

    /**
     * The ClientHandler sends a message to the client.
     * @param message The message that is sent to the client.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Terminates the connection over the socket.
     */
    public void terminate() {
        log.info("Attempting to terminate clientHandler");
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket", e);
        }
        this.terminate = true;
    }
}
