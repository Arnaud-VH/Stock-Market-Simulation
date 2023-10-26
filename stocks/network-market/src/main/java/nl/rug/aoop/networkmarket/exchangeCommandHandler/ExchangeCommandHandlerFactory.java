package nl.rug.aoop.networkmarket.exchangeCommandHandler;

import nl.rug.aoop.networkmarket.exchangeCommandHandler.commands.PlaceAskCommand;
import nl.rug.aoop.networkmarket.exchangeCommandHandler.commands.RegisterCommand;
import nl.rug.aoop.networkmarket.clientUpdater.ExchangeServer;
import nl.rug.aoop.networkmarket.exchangeCommandHandler.commands.PlaceBidCommand;
import nl.rug.aoop.messagequeue.commandhandler.AbstractCommandHandlerFactory;

/**
 * Factory that creates the commands for the Exchange.
 */
public class ExchangeCommandHandlerFactory implements AbstractCommandHandlerFactory {
    private final ExchangeServer exchange;

    /**
     * The constructor for the Exchange command handler factory.
     * @param exchange The exchange on which the queue commands will be executed.
     */
    public ExchangeCommandHandlerFactory(ExchangeServer exchange) {
        this.exchange = exchange;
    }

    @Override
    public ExchangeCommandHandler createCommandHandler() {
        ExchangeCommandHandler exchangeCommandHandler = ExchangeCommandHandler.getInstance();
        exchangeCommandHandler.registerCommand("PlaceBid", new PlaceBidCommand(exchange));
        exchangeCommandHandler.registerCommand("PlaceAsk", new PlaceAskCommand(exchange));
        exchangeCommandHandler.registerCommand("register", new RegisterCommand(exchange));
        return exchangeCommandHandler;
    }
}
