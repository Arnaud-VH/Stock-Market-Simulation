package serverExchange.commandHandler.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Ask;

import java.io.IOException;
import java.util.Map;

import serverExchange.commandHandler.MarketSerializer;

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
        } catch (ClassNotFoundException e) {
            log.error("Unable to execute client place ask command: ", e);
        } catch (IOException e) {
            log.error("Unable to execute client place ask command: ", e);
        }
    }
}
