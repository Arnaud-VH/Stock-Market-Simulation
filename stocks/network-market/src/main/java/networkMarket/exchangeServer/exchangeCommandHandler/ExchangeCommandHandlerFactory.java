package networkMarket.exchangeServer.exchangeCommandHandler;

import networkMarket.exchangeServer.ExchangeServer;
import networkMarket.exchangeServer.exchangeCommandHandler.commands.PlaceAskCommand;
import networkMarket.exchangeServer.exchangeCommandHandler.commands.PlaceBidCommand;
import networkMarket.exchangeServer.exchangeCommandHandler.commands.RegisterCommand;
import nl.rug.aoop.messagequeue.CommandHandler.AbstractCommandHandlerFactory;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandler;

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
    public QueueCommandHandler createCommandHandler() {
        QueueCommandHandler queueCommandHandler = QueueCommandHandler.getInstance();
        queueCommandHandler.registerCommand("PlaceBid", new PlaceBidCommand(exchange));
        queueCommandHandler.registerCommand("PlaceAsk", new PlaceAskCommand(exchange));
        queueCommandHandler.registerCommand("register", new RegisterCommand(exchange));
        return queueCommandHandler;
    }
}
