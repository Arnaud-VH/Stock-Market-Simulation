package nl.rug.aoop.networking.Client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

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
    private boolean running = false;
    @Getter
    private boolean connected = false;
    private BufferedReader in;
    private PrintWriter out;
    private final MessageHandler messageHandler;

    /**
     * Constructor for the client that connects to the server.
     * @param address Address for hostName and port number.
     * @param messageHandler Handles the communication between client to server.
     * @throws IOException Exception in case it was not able to connect to the Server Socket.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        initSocket(address);
    }

    private void initSocket(InetSocketAddress address) throws IOException {
        Socket socket = new Socket();
        socket.connect(address, TIMEOUT);
        if (!socket.isConnected()) {
            log.error("Socket could not connect to port: " + address.getPort());
            throw new IOException("Socket could not connect");
        }
        connected = true;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Run function that keeps connection between client and server and gets the input.
     */
    @Override
    public void run() {
        running = true;
        //Server sends a message
        while(running) {
            try {
                String fromServer = in.readLine();
                if (fromServer == null) {
                    terminate();
                    break;
                }
                log.info("Server sent: " + fromServer);
                //Get input from user and send to server
                messageHandler.handleMessage(fromServer);
                //Handle message can be a command map (Using the command pattern), that executes the commands.
            } catch (IOException e) {
                log.error("Could not read line from server: ", e);
            }
        }

    }

    /**
     * Sends a message to the server.
     * @param message The message that is sent to the server.
     */
    public void sendMessage(String message) {
        if (message == null || message.equalsIgnoreCase("")) {
            throw new IllegalArgumentException("Attempting to send an invalid message");
        }
        out.println(message);
    }

    /**
     * Called when we close the connection. It is no longer running.
     */
    public void terminate() {
        running = false;
    }

}
