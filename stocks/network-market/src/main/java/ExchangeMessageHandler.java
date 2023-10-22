import lombok.Getter;
import nl.rug.aoop.command.Command.CommandHandler;
import nl.rug.aoop.messagequeue.Consumers.Consumer;
import nl.rug.aoop.messagequeue.Queues.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles incoming MQ Messages for the ExchangeServer.
 */
public class ExchangeMessageHandler implements Runnable {
    private final Consumer consumer;
    private final CommandHandler commandHandler;
    private volatile boolean terminate = false;
    @Getter private volatile boolean running = false;

    /**
     * The constructor for the Exchange Message Handler.
     * @param consumer The consumer that is receiving the commands and messages.
     * @param commandHandler The command handler that handles the commands for the consumer.
     */
    public ExchangeMessageHandler(Consumer consumer, CommandHandler commandHandler) {
        this.consumer = consumer;
        this.commandHandler = commandHandler;
    }

    /**
     * Continuously polls MQ and handles messages.
     */
    @Override
    public void run() {
        running = true;
        while (!terminate) {
            Message clientMessage = getClientCommand();
            Map<String,Object> args = new HashMap<>();
            if (clientMessage != null) {
                args.put("header",clientMessage.getHeader());
                args.put("body",clientMessage.getBody());
                commandHandler.execute(clientMessage.getHeader(),args);
            }
        }
        running = false;
    }

    /**
     * Polls MQ until it contains a message in which case it gets returned.
     * Special behaviour when Class gets terminated in which case it can return a null.
     * @return Message to be handeled.
     */
    private Message getClientCommand() {
        Message polled = consumer.poll();
        while (polled == null && !terminate) {
            polled = consumer.poll();
        }
        return polled;
    }

    /**
     * Terminates the handling of messages.
     */
    public void terminate() {
        this.terminate = true;
    }
}
