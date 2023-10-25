package networkMarket.TraderClient.TraderCommandHandler;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.command.command.CommandHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * The trader commandHandler handles commands for the trader Client.
 */
@Slf4j
public class TraderCommandHandler implements CommandHandler {
    private final Map<String, Command> commandMap;

    /**
     * Constructor for the command handler.
     */
    public TraderCommandHandler() {
        this.commandMap = new HashMap<>();
    }

    /**
     * Registers a command into the command handlers commandMap.
     * @param command The command key being registered.
     * @param commandClass The actual command that can be executed.
     */
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
