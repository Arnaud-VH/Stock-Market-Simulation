package nl.rug.aoop.networking.Server;

import nl.rug.aoop.networking.Handlers.MessageHandler;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientHandler {
    private InputStream mockSocketInput;
    private BufferedReader msgFromClientHandler;
    private MessageHandler mockHandler;
    private Socket mockSocket;

    @BeforeEach
    public void setup() throws IOException {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream();  // out is outputstream of mockSocket
        inputStream.connect(outputStream);
        this.mockSocket = Mockito.mock(Socket.class);

        InputStream mockSocketInput = new ByteArrayInputStream("hello world".getBytes()); // message to give to handler
        Mockito.when(mockSocket.getInputStream()).thenReturn(mockSocketInput);
        Mockito.when(mockSocket.getOutputStream()).thenReturn(outputStream);
        this.msgFromClientHandler = new BufferedReader(new InputStreamReader(inputStream)); // message sent by handler
        this.mockHandler = Mockito.mock(MessageHandler.class);
    }
    @Test
    public void testConstructor() throws IOException {
        ClientHandler clientHandler = new ClientHandler(mockSocket,0,mockHandler);
        assertNotNull(clientHandler);
    }

    @Test
    public void testMessageHandled() throws IOException {
        ClientHandler clientHandler = new ClientHandler(mockSocket,0,mockHandler);
        clientHandler.run();
        Mockito.verify(mockHandler).handleMessage("hello world");
    }

    @Test
    public void testEndConnection() throws IOException {
        mockSocketInput = new ByteArrayInputStream("".getBytes());
        Mockito.when(mockSocket.getInputStream()).thenReturn(mockSocketInput);
        ClientHandler clientHandler = new ClientHandler(mockSocket,0,mockHandler);
        clientHandler.run();
        Mockito.verify(mockHandler,Mockito.never()).handleMessage(Mockito.anyString());
    }

    @Test
    public void testID() throws IOException {
        String expectedString = new NetworkMessage("client_id","5").toJson();
        ClientHandler clientHandler = new ClientHandler(mockSocket,5,mockHandler);
        clientHandler.run();
        assertEquals(expectedString, msgFromClientHandler.readLine());
    }

    @Test
    public void testEcho() throws IOException {
        String expectedString = new NetworkMessage("echo","hello world").toJson();
        ClientHandler clientHandler = new ClientHandler(mockSocket,5,mockHandler);
        clientHandler.run();
        msgFromClientHandler.readLine();
        assertEquals(expectedString, msgFromClientHandler.readLine());
    }
}
