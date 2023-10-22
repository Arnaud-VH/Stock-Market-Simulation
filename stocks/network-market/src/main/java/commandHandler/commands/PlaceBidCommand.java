package commandHandler.commands;

import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Bid;

import java.util.Map;

public class PlaceBidCommand implements Command {
    private final Exchange exchange;
    public PlaceBidCommand (Exchange exchange) {
        this.exchange = exchange;
    }
    @Override
    public void execute(Map<String, Object> params) {
        exchange.placeBid((Bid)params.get("body"));
    }
}
