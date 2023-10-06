package nl.rug.aoop.networking.Client;

import nl.rug.aoop.networking.Command.CommandHandler;

import java.util.Map;

public class CommandMessageHandler implements MessageHandler{
    private final CommandHandler commandHandler;
    private Map<String, Object> args;

    public CommandMessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    @Override
    public void handleMessage(String json) {
        NetworkMessage message = NetworkMessage.fromJson(json);
        args.put("header", message.getHeader());
        args.put("body", message.getBody());        // read over end of command implementation did not do the interface part or whatever
        commandHandler.execute(message.getHeader(), args);
    }
}
