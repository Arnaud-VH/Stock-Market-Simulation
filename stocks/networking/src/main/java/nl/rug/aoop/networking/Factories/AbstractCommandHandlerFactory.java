package nl.rug.aoop.networking.Factories;

import nl.rug.aoop.networking.Command.CommandHandler;

/**
 * Interface to implement the factory design pattern. Allows us to create commands.
 */
public interface AbstractCommandHandlerFactory {
    CommandHandler createCommandHandler();
}
