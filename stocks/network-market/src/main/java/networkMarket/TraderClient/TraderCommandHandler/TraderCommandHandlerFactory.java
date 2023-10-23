package networkMarket.TraderClient.TraderCommandHandler;

import networkMarket.TraderClient.TraderClient;
import networkMarket.TraderClient.TraderCommandHandler.Commands.UpdateCommand;
import nl.rug.aoop.messagequeue.CommandHandler.AbstractCommandHandlerFactory;

public class TraderCommandHandlerFactory implements AbstractCommandHandlerFactory {
    private volatile TraderClient trader;

    public TraderCommandHandlerFactory(TraderClient trader) {
        this.trader = trader;
    }

    @Override
    public TraderCommandHandler createCommandHandler() {
        TraderCommandHandler traderCommandHandler = TraderCommandHandler.getInstance();
        traderCommandHandler.registerCommand("update", new UpdateCommand(trader));
        return traderCommandHandler;
    }
}
