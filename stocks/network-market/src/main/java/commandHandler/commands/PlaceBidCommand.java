package commandHandler.commands;

import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Bid;

import java.util.Map;

/**
 * Command that places a bid into the exchange.
 */
public class PlaceBidCommand implements Command {
    private final Exchange exchange;

    /**
     * Constructor for the place bid command.
     * @param exchange The exchange onto which the bid is placed.
     */
    public PlaceBidCommand(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void execute(Map<String, Object> params) {
        exchange.placeBid((Bid)params.get("body"));
    }
}
