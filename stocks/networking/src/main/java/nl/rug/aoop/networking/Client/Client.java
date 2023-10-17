package nl.rug.aoop.networking.Client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;

/**
 * Client class that connects with the server.
 */
@Slf4j
public class Client implements Runnable{
    /**
     * Constant TIMEOUT.
     */
    public static final int TIMEOUT = 1000;
    @Getter
    private volatile boolean running = false;
    private volatile Boolean terminate = false;
    private BufferedReader in;
    private PrintWriter out;
    private final MessageHandler messageHandler;
    private final InetSocketAddress address;
    private Socket socket;

    /**
     * Constructor for the client that connects to the server.
     * @param address Address for hostName and port number.
     * @param messageHandler Handles the communication between client to server.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.address = address;
    }

    private void initSocket(InetSocketAddress address) {
        this.socket = new Socket();
        try {
            socket.connect(address, TIMEOUT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            log.info("Connected to server");
        } catch (SocketTimeoutException e) {
            log.error("Connection timed out: ", e);
        } catch (IOException e) {
            log.error("Couldn't connect: ", e);
        }
    }

    /**
     * Run function that keeps connection between client and server and gets the input.
     */
    @Override
    public void run() {
        initSocket(address);
        while(!terminate) {
            if(!socket.isConnected()) {
                log.info("Connection to server lost");
                break;
            }
            running = true;
            try {
                String fromServer = in.readLine();
                messageHandler.handleMessage(fromServer);
            } catch (IOException e) {
                log.error("Could not read line from server: ", e);
            }
        }
        running = false;
        log.info("Client terminated");
    }

    /**
     * Sends a message to the server.
     * @param message The message that is sent to the server.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Called when we close the connection. It is no longer running.
     */
    public void terminate() {
        try {
            in.close();
        } catch (IOException e) {
            log.error("Unable to close BufferedReader: ", e);
        }
        terminate = true;
    }

}
