package nl.rug.aoop.command.command;

import java.util.Map;

/**
 * CommandHandler interface that indicates behaviour of Handlers to execute commands.
 */
public interface CommandHandler {

    /**
     * Execute method that must be implemented.
     * @param commandKey The key that links to the command.
     * @param args The arguments of the command that has to be executed.
     */
    void execute(String commandKey, Map<String, Object> args);
}
