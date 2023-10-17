package nl.rug.aoop.networking.Server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;

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
        out.println(new NetworkMessage("client_id",Integer.toString(id)).toJson());  // send client's id to client
        while(!terminate) {
            try {
                String fromClient = in.readLine();
                if (fromClient == null) {  // force close connection if client sends null. Connections should be closed through server
                    log.info("Terminating clientHandler '" + id + "' because client sent null");
                    terminate();
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
     * Terminates the connection over the socket.
     */
    public void terminate() {
        terminate = true;
        try { in.close(); } catch (IOException e) { log.error("Could not close the BufferedReader", e); }  // ensuring run() doesnt stop when reading input
        try { this.socket.close(); } catch (IOException e) { log.error("Could not close the socket", e); }
    }
}
