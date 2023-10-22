package commandHandler.commands;

import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;

import java.util.Map;

public class PlaceAskCommand implements Command {
    private final Exchange exchange;
    public PlaceAskCommand (Exchange exchange) {
        this.exchange = exchange;
    }
    @Override
    public void execute(Map<String, Object> params) {
        exchange.placeAsk((Ask)params.get("body"));
    }
}
