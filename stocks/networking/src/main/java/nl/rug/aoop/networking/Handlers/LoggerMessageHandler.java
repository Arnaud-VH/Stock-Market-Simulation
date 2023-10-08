package nl.rug.aoop.networking.Handlers;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of messageHandler that is used for server to client communication of message.
 */
@Slf4j
public class LoggerMessageHandler implements MessageHandler {

    /**
     * Handler method that is overridden from interface.
     * @param message The String message sent from the server.
     */
    @Override
    public void handleMessage(String message) {
        log.info("Got message: " + message);
    }
}
