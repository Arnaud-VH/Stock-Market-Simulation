package commandHandler.commands;

import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Ask;

import java.util.Map;

/**
 * Command that places an ask into the exchange.
 */
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
        exchange.placeAsk((Ask)params.get("body"));
    }
}
