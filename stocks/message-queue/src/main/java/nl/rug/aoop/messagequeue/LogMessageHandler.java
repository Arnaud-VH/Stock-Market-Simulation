package nl.rug.aoop.messagequeue;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;

@Slf4j
public class LogMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(String message) {
        log.info("message received: " + message);
    }
}
