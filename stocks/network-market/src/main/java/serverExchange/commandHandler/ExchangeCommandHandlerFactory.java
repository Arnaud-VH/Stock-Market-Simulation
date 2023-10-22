package serverExchange.commandHandler;

import serverExchange.commandHandler.commands.PlaceAskCommand;
import serverExchange.commandHandler.commands.PlaceBidCommand;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.messagequeue.CommandHandler.AbstractCommandHandlerFactory;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandler;

public class ExchangeCommandHandlerFactory implements AbstractCommandHandlerFactory {
    private Exchange exchange;

    /**
     * The constructor for the Exchange command handler factory.
     * @param exchange The exchange on which the queue commands will be executed.
     */
    public ExchangeCommandHandlerFactory(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public QueueCommandHandler createCommandHandler() {
        QueueCommandHandler queueCommandHandler = QueueCommandHandler.getInstance();
        queueCommandHandler.registerCommand("PlaceBid", new PlaceBidCommand(exchange));
        queueCommandHandler.registerCommand("PlaceAsk", new PlaceAskCommand(exchange));
        return queueCommandHandler;
    }
}
