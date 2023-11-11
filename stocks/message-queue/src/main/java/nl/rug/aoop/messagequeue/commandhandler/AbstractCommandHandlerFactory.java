package nl.rug.aoop.messagequeue.commandhandler;

import nl.rug.aoop.command.command.CommandHandler;

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
