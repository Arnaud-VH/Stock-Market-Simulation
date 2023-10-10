package nl.rug.aoop.messagequeue;

import nl.rug.aoop.command.Command.CommandHandler;
import nl.rug.aoop.networking.Handlers.MessageHandler;

import java.util.HashMap;
import java.util.Map;

public class CommandMessageHandler implements MessageHandler {

    private final CommandHandler commandHandler;
    private Map<String, Object> args;
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
