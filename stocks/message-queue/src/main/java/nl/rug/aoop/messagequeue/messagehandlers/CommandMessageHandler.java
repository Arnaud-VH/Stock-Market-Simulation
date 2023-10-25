package nl.rug.aoop.messagequeue.messagehandlers;

import nl.rug.aoop.command.command.CommandHandler;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.handlers.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles messages by converting them into Json's and executes them from the Command Handler.
 */
public class CommandMessageHandler implements MessageHandler {

    private final CommandHandler commandHandler;
    private Map<String, Object> args;

    /**
     * Constructor for the CommandMessageHandler.
     * @param commandHandler The commandHandler that will execute the converted Json Message.
     */
    public CommandMessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handleMessage(String json) {
        Message msg = Message.fromJson(json);
        Map<String, Object> map = new HashMap<>();
        map.put("header", msg.getHeader());
        map.put("body", msg.getBody());
        // TODO figure out the interface to put in the map as described in assignment
        commandHandler.execute(msg.getHeader(),map);
    }
}
