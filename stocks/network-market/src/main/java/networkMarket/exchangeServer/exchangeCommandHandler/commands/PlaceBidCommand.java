package networkMarket.exchangeServer.exchangeCommandHandler.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Bid;
import networkMarket.MarketSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * Command that places a bid into the exchange.
 */
@Slf4j
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
        try {
            Bid bid = MarketSerializer.fromString((String)params.get("body"),Bid.class);
            exchange.placeBid(bid);
        } catch (ClassNotFoundException | IOException e) {
            log.error("Unable to execute client place bid command, failed to deserialize Bid ", e);
        }
    }
}
