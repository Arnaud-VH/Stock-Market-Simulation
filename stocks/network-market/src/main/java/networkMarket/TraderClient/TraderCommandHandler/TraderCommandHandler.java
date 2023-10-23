package networkMarket.TraderClient.TraderCommandHandler;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.command.Command.CommandHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TraderCommandHandler implements CommandHandler {
    private final Map<String, Command> commandMap;
    private static final TraderCommandHandler COMMAND_HANDLER = new TraderCommandHandler();

    private TraderCommandHandler() {
        this.commandMap = new HashMap<>();
    }

    public static TraderCommandHandler getInstance() {
        return COMMAND_HANDLER;
    }

    public void registerCommand(String command, Command commandClass) {
        this.commandMap.put(command, commandClass);
    }

    @Override
    public void execute(String commandKey, Map<String, Object> args) {
        try {
            commandMap.get(commandKey).execute(args);
        } catch (NullPointerException e) {
            log.error("Invalid command: " + e);
        } catch (ClassCastException e) {
            log.error("Inappropriate command  key type: ", e);
        }
    }
}
