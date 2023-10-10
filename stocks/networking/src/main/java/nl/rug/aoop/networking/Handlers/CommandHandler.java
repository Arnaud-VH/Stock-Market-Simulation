package nl.rug.aoop.networking.Handlers;

import java.util.Map;

/**
 * Command Handler interface that deals with the execution of commands.
 */
public interface CommandHandler {

    /**
     * Interface method execute that performs the command.
     * @param commandKey The key that maps to the specific command.
     * @param args The arugments of the command that have to be executed.
     */
    void execute(String commandKey, Map<String, Object> args);
}
