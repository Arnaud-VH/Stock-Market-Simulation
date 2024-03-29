package nl.rug.aoop.messagequeue.commandhandler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.command.command.CommandHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Command handler deals with the commands that the client wants to execute.
 */
@Getter
@Slf4j
public class QueueCommandHandler implements CommandHandler {
    private final Map<String, Command> commandMap;
    private static final QueueCommandHandler COMMAND_HANDLER = new QueueCommandHandler();

    /**
     * Constructor for the command Handler. Here we instantiate the commandMap to a hashMap.
     */
    private QueueCommandHandler() {
        this.commandMap = new HashMap<>();
    }

    public static QueueCommandHandler getInstance() {
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
    * Executes the command.
    * @param commandKey the key of the command that is to be executed.
    * @param args The arguments of the command that will be executed.
    */
    @Override
    public void execute(String commandKey, Map<String, Object> args) {
        if(commandMap.containsKey(commandKey)) {
            log.info("Executing command: " + commandKey);
            Command command1 = commandMap.get(commandKey);
            command1.execute(args);
        } else {
            log.info("Client asked for command that does not exist: " + commandKey);
        }
    }
}
