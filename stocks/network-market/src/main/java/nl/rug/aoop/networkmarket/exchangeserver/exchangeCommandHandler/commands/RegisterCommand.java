package nl.rug.aoop.networkmarket.exchangeserver.exchangeCommandHandler.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networkmarket.serialiser.MarketSerializer;
import nl.rug.aoop.networkmarket.exchangeserver.ExchangeServer;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.market.trader.Trader;

import java.io.IOException;
import java.io.Serializable;
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
            ArrayList<Serializable> list = MarketSerializer.fromString((String) params.get("body"), ArrayList.class);
            Trader trader = (Trader)list.get(0);
            int clientID = (int)list.get(1);
            exchangeServer.registerTrader(trader, clientID);
            log.info("Registered Trader " + trader.getId() + " to clientID " + clientID);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Unable to execute register client command, failed to deserialize IDs ", e);
        }
    }
}
