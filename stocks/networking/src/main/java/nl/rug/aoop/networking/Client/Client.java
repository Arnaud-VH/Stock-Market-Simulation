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
import java.util.Timer;
import java.util.TimerTask;

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
    @Getter private volatile int id;

    /**
     * Constructor for the client that connects to the server.
     * @param address Address for hostName and port number.
     * @param messageHandler Handles the communication between client to server.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.address = address;
        initSocket(address);
    }

    private void initSocket(InetSocketAddress address) {
        this.socket = new Socket();
        try {
            socket.connect(address, TIMEOUT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            log.info("Connected to server");
        } catch (SocketTimeoutException e) {
            log.error("Connection to server timed out: ", e);
        } catch (IOException e) {
            log.error("Couldn't connect to server: ", e);
        }

        Thread thread = new Thread(() -> {
            try {
                id = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                log.error("Unable to read client id: ", e);
            }
        });
        thread.start();
        Timer timer = new Timer();
        TimeOutTask timeoutTask = new TimeOutTask(thread, timer);
        timer.schedule(timeoutTask,1500);
    }

    /**
     * Run function that keeps connection between client and server and gets the input.
     */
    @Override
    public void run() {
        if(!isConnected()) {
            log.info("Client not connected");
            terminate = true;
        }
        while(!terminate) {
            if(!isConnected()) {
                log.info("Connection to server lost");
                break;
            }
            running = true;
            try {
                String fromServer = in.readLine();
                messageHandler.handleMessage(fromServer);
            } catch (IOException e) {
                if (!terminate) {
                    log.error("Could not read line from server: ", e);
                }
            }
        }
        log.info("Client terminated");
        running = false;
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
            socket.close();
        } catch (IOException e) {
            log.error("Unable to close BufferedReader: ", e);
        }
        terminate = true;
    }

    public Boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * Util class to be able to abort reading from the server after timeout.
     */
    private class TimeOutTask extends TimerTask {
        private final Thread thread;
        private final Timer timer;

        TimeOutTask(Thread thread, Timer timer) {
            this.thread = thread;
            this.timer = timer;
        }

        @Override
        public void run() {
            if(thread != null && thread.isAlive()) {
                thread.interrupt();
                timer.cancel();
            }
        }
    }
}
