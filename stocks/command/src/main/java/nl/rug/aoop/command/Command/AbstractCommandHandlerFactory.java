package nl.rug.aoop.command.Command;

import nl.rug.aoop.command.Command.CommandHandler;

/**
 * Interface to implement the factory design pattern. Allows us to create commands.
 */
public interface AbstractCommandHandlerFactory {

    /**
     * Initialises a commandHandler for a set of commands.
     * @return The created command Handler.
     */
    CommandHandler createCommandHandler();
}
