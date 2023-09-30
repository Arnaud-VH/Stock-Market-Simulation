package nl.rug.aoop.networking.Server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Command.CommandHandler;

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
    private Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running = false;
    private final int id;
    private final CommandHandler commandHandler;

    /**
     * Constructor for the clientHandler.
     * @param socket The socket through which communication with client and server happens.
     * @param id The ID of the client that is communicating with the server.
     * @param commandHandler The commandHandler instance that deals with client's commands.
     * @throws IOException The exception that results from the socket input and output stream.
     */
    public ClientHandler(Socket socket, int id, CommandHandler commandHandler) throws IOException {
        this.socket = socket;
        this.id = id;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true); //Sets autoflush to true
        //This allows us to write and read from and to the socket.
        this.commandHandler = commandHandler;
    }

    @Override
    //We implement the runnable interface later.
    public void run() {
        running = true;
        // Write the output
        //Read the input and echo back
        out.println("Hello, enter BYE to exit. Your ID is: " + id); //This is our PrintWriter, id from the user.
        while(running) {
            try {
                String fromClient = in.readLine();
                //Client handler ECHO's back
                if (fromClient == null || fromClient.trim().equalsIgnoreCase("BYE")) {
                    //Here we should use the command pattern, the word BYE is mapped to a class that executes terminate.
                    //Client is trying to disconnect, running should be false.
                    terminate();
                    //Need to break because otherwise we try to write to a socket we closed in terminate().
                    break;
                }
                log.info("Received message" + fromClient);
                commandHandler.execute(fromClient);
                //commandMap.get(fromClient.trim()).execute();
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
