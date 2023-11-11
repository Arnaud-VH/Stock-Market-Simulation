package nl.rug.aoop.networkmarket.traderclient.tradercommandhandler.Commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.networkmarket.traderclient.TraderClient;
import nl.rug.aoop.util.serialiser.MarketSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Update command updates the trader with the relevant information.
 */
@Slf4j
public class UpdateCommand implements Command {
    private volatile TraderClient trader;

    /**
     * Constructor for the update command.
     * @param trader The trader whose information is being updated.
     */
    public UpdateCommand(TraderClient trader) {
        this.trader = trader;
    }

    @Override
    public void execute(Map<String, Object> params) {
        try {
            ArrayList<Serializable> arrayList =
                    MarketSerializer.fromString((String)params.get("body"), ArrayList.class);
            ArrayList<Stock> newStocks = (ArrayList<Stock>)arrayList.get(0);
            Trader networkedTrader = (Trader)arrayList.get(1);
            this.trader.updateTrader(networkedTrader.getFunds(), networkedTrader.getOwnedStocks());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Could not execute command because deserialization of arrayList went wrong: ", e);
        }

    }
}
