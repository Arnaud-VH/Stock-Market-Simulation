package networkMarket.TraderClient.TraderCommandHandler;

import networkMarket.TraderClient.TraderClient;
import networkMarket.TraderClient.TraderCommandHandler.Commands.ClientIDCommand;
import networkMarket.TraderClient.TraderCommandHandler.Commands.EchoCommand;
import networkMarket.TraderClient.TraderCommandHandler.Commands.UpdateCommand;
import nl.rug.aoop.messagequeue.commandhandler.AbstractCommandHandlerFactory;

/**
 * Factory that creates the commands for the traderCommand handler.
 */
public class TraderCommandHandlerFactory implements AbstractCommandHandlerFactory {
    private volatile TraderClient trader;

    /**
     * Constructor for the trader command handler factor.
     * @param trader The trader that the command handler works with.
     */
    public TraderCommandHandlerFactory(TraderClient trader) {
        this.trader = trader;
    }

    @Override
    public TraderCommandHandler createCommandHandler() {
        TraderCommandHandler traderCommandHandler = new TraderCommandHandler();
        traderCommandHandler.registerCommand("update", new UpdateCommand(trader));
        traderCommandHandler.registerCommand("echo", new EchoCommand());
        traderCommandHandler.registerCommand("client_id", new ClientIDCommand(trader));
        return traderCommandHandler;
    }
}
