package nl.rug.aoop.networking.Server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClientHandler {
    private InputStream mockSocketInput;
    private BufferedReader msgFromClientHandler;
    private PrintWriter msgToClientHandler;
    private MessageHandler mockHandler;
    private Socket mockSocket;
    private ClientHandler clientHandler;
    private Thread thread;

    private void setup() {
        try {
            PipedInputStream fromClientHandler = new PipedInputStream();
            PipedOutputStream referenceForClientHandler = new PipedOutputStream();  // out is outputstream of mockSocket
            fromClientHandler.connect(referenceForClientHandler);

            PipedInputStream fromTest = new PipedInputStream();
            PipedOutputStream referenceForTest = new PipedOutputStream();
            fromTest.connect(referenceForTest);

            msgFromClientHandler = new BufferedReader(new InputStreamReader(fromClientHandler));
            msgToClientHandler = new PrintWriter(referenceForTest, true);

            this.mockSocket = Mockito.mock(Socket.class);
            Mockito.when(mockSocket.getInputStream()).thenReturn(fromTest);
            Mockito.when(mockSocket.getOutputStream()).thenReturn(referenceForClientHandler);

            this.mockHandler = Mockito.mock(MessageHandler.class);
        } catch (Exception e) {
            log.error("test setup failed: ", e);
        }

    }

    private void startClientHandler() {
        setup();
        try {
            this.clientHandler = new ClientHandler(mockSocket,0,mockHandler);
            this.thread = new Thread(clientHandler);
            thread.start();
            await().atMost(Duration.ofSeconds(1)).until(clientHandler::isRunning);
        } catch (IOException e) {
            log.error("couldn't start test clientHandler: ", e);
        }

    }

    @Test
    public void testConstructor() throws IOException {
        ClientHandler clientHandler = new ClientHandler(mockSocket,0,mockHandler);
        assertNotNull(clientHandler);
    }

    @Test
    public void testMessageHandled() {
        startClientHandler();

        msgToClientHandler.println("hello world");
        Mockito.verify(mockHandler,Mockito.timeout(1000).atLeastOnce()).handleMessage("hello world");
        clientHandler.terminate();
    }

    @Test
    public void testTerminate() throws IOException {
        startClientHandler();

        clientHandler.terminate();
        Mockito.verify(mockSocket).close();
        msgToClientHandler.println("arbitrary_string");
        await().atMost(Duration.ofSeconds(2)).until(() -> !thread.isAlive());
    }

    @Test
    public void testEndConnection() {
        startClientHandler();

        msgToClientHandler.close(); // sends end stream byte
        await().atMost(Duration.ofSeconds(1)).until(() -> !clientHandler.isRunning());
        assertTrue(!thread.isAlive() && !clientHandler.isRunning());
    }

    @Test
    public void testID() throws IOException {
        startClientHandler();

        String expectedString = new NetworkMessage("client_id","0").toJson();
        assertEquals(expectedString, msgFromClientHandler.readLine());
        clientHandler.terminate();
    }

    @Test
    public void testEcho() throws IOException {
        startClientHandler();

        String expectedString = new NetworkMessage("echo","hello world").toJson();
        msgToClientHandler.println("hello world");
        msgFromClientHandler.readLine(); // consume line that gives id

        assertEquals(expectedString, msgFromClientHandler.readLine());
        clientHandler.terminate();
    }

    @Test
    public void testSendMessage() throws IOException {
        startClientHandler();
        clientHandler.sendMessage("hello world");
        msgFromClientHandler.readLine(); // consume id
        assertEquals("hello world", msgFromClientHandler.readLine());
        clientHandler.terminate();
    }
}
