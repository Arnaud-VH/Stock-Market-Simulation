package networkMarket.exchangeServer.exchangeCommandHandler.commands;

import lombok.extern.slf4j.Slf4j;
import networkMarket.MarketSerializer;
import networkMarket.exchangeServer.clientUpdater.ExchangeServer;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Trader.Trader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Commands that registers a traderClient with the server.
 */
@Slf4j
public class RegisterCommand implements Command {
    private final ExchangeServer exchangeServer;

    /**
     * Constructor for the Register Command.
     * @param exchangeServer The exchange server that receives the command.
     */
    public RegisterCommand(ExchangeServer exchangeServer) {
        this.exchangeServer = exchangeServer;
    }

    @Override
    public void execute(Map<String, Object> params) {
        try {
            ArrayList<String> list = MarketSerializer.fromString((String) params.get("body"), ArrayList.class);
            Trader trader = MarketSerializer.fromString(list.get(0), Trader.class);
            int clientID = Integer.parseInt(list.get(1));
            exchangeServer.registerTrader(trader, clientID);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Unable to execute register client command, failed to deserialize IDs ", e);
        }
    }
}
