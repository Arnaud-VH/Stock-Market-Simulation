package serverExchange.commandHandler.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Bid;

import java.util.Map;

public class PlaceBidCommand implements Command {
    private final Exchange exchange;
    private final Gson gson;
    public PlaceBidCommand (Exchange exchange) {
        this.exchange = exchange;
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        this.gson = builder.create();
    }
    @Override
    public void execute(Map<String, Object> params) {
        Bid bid = gson.fromJson((String)params.get("body"),Bid.class);
        exchange.placeBid(bid);
    }
}
