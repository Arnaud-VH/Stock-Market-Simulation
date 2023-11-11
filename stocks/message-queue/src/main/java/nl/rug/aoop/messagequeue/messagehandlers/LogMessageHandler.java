package nl.rug.aoop.messagequeue.messagehandlers;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.handlers.MessageHandler;

/**
 * Message Handler that log's the messages that are received by the client.
 */
@Slf4j
public class LogMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {
        log.info("message received: " + message);
    }
}
