package serverExchange.commandHandler.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Transaction.Ask;
import serverExchange.commandHandler.MarketSerializer;


import java.util.Map;

@Slf4j
public class PlaceAskCommand implements Command {
    private final Exchange exchange;
    public PlaceAskCommand (Exchange exchange) {
        this.exchange = exchange;
    }
    @Override
    public void execute(Map<String, Object> params) {
        try {
            Ask ask = MarketSerializer.fromString((String)params.get("body"),Ask.class);
            exchange.placeAsk(ask);
        } catch (Exception e) {
            log.error("Unable to execute client place ask command: ", e);
        }
    }
}
