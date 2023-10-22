package networkMarket.exchangeServer.exchangeCommandHandler;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.command.Command.CommandHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Command Handler for the exchange commands.
 */
@Slf4j
public class ExchangeCommandHandler implements CommandHandler {

    private final Map<String, Command> commandMap;
    private static final ExchangeCommandHandler COMMAND_HANDLER = new ExchangeCommandHandler();

    /**
     * Constructor for the command Handler. Here we instantiate the commandMap to a hashMap.
     */
    private ExchangeCommandHandler() {
        this.commandMap = new HashMap<>();
    }

    public static ExchangeCommandHandler getInstance() {
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
        try {
            log.info("Attempting to execute command " + commandKey + ".");
            commandMap.get(commandKey).execute(args);
        } catch (ClassCastException e) {
            log.info("Inappropriate command key type: ", e);
        } catch (NullPointerException e) {
            log.info("Invalid command: ", e);
        }
    }
}
