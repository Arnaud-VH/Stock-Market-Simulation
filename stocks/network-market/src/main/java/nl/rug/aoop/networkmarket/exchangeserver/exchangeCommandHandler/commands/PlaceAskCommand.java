package nl.rug.aoop.networkmarket.exchangeserver.exchangeCommandHandler.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.market.exchange.Exchange;
import nl.rug.aoop.market.transaction.Ask;
import nl.rug.aoop.util.serialiser.MarketSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * Command that places an ask into the exchange.
 */
@Slf4j
public class PlaceAskCommand implements Command {
    private final Exchange exchange;

    /**
     * Constructor for this command.
     * @param exchange The exchange onto which the ask is placed.
     */
    public PlaceAskCommand(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void execute(Map<String, Object> params) {
        try {
            Ask ask = MarketSerializer.fromString((String)params.get("body"),Ask.class);
            exchange.placeAsk(ask);
        } catch (ClassNotFoundException | IOException e) {
            log.error("Unable to execute client place ask command, failed to deserialize Ask: ", e);
        }
    }
}
