package nl.rug.aoop.networking.Handlers;

import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;

import java.util.Map;

/**
 * Handles incoming messages to the server.
 */
public class CommandMessageHandler implements MessageHandler {
    private final CommandHandler commandHandler;
    private Map<String, Object> args;

    /**
     * Constructor for the CommandMessageHandler.
     * @param commandHandler The commandHandler from the server that is used.
     */
    public CommandMessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handleMessage(String json) {
        NetworkMessage message = NetworkMessage.fromJson(json);
        args.put("header", message.getHeader());
        args.put("body", message.getBody());
        // read over end of command implementation did not do the interface part or whatever
        commandHandler.execute(message.getHeader(), args);
    }
}
