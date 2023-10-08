package nl.rug.aoop.networking.Command;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Command handler deals with the commands that the client wants to execute.
 */
@Slf4j
public class CommandHandler {
    private final Map<String, Command> commandMap;
    private static final CommandHandler COMMAND_HANDLER = new CommandHandler();

    /**
     * Constructor for the command Handler. Here we instantiate the commandMap into a hashMap.
     */
    private CommandHandler() {
        this.commandMap = new HashMap<>();
    }

    public static CommandHandler getInstance() {
        return COMMAND_HANDLER;
    }

    /**
     * Allows us to register commands into the commandMap.
     * @param command The string command, works as a key.
     * @param commandClass The class that models the command we want to execute.
     */
    public void registerCommand(String command, Command commandClass) {
        this.commandMap.put(command, commandClass);
    }

    /**
     /**
     * Executes the command.
     * @param commandKey the key of the command that is to be executed.
     * @param args The arguments of the command that will be executed.
     */
    public void execute(String commandKey, Map<String, Object> args) {
        if(commandMap.containsKey(commandKey)) {
            Command command1 = commandMap.get(commandKey);
            command1.execute(args);
            log.info("Command has been executed: " + commandKey);
        } else {
            log.info("Client asked for command that does not exist: " + commandKey);
        }
    }
}
